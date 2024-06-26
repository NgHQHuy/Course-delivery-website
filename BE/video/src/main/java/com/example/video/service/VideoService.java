package com.example.video.service;

import com.example.video.VideoApplication;
import com.example.video.entity.Video;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class VideoService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;
    public String addVideo(String title, MultipartFile file, Long lectureId) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("type", "video");
        metaData.put("title", title);
        metaData.put("lectureId", lectureId);
        ObjectId id = gridFsTemplate.store(
                file.getInputStream(), file.getName(), file.getContentType(), metaData
        );
        return id.toString();
    }

    public Video getVideo(String id) throws IllegalStateException, IOException {
        GridFSFile file = gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(id))
        );
        Video video = new Video();
        video.setTitle(file.getMetadata().get("title").toString());
        video.setLength(file.getLength());
        video.setChunkLength(file.getChunkSize());
        video.setContentType(file.getMetadata().get("_contentType").toString());
        video.setStream(gridFsOperations.getResource(file).getInputStream());
        return video;
    }

    public Video getVideoWithLectureId(Long id) throws IllegalStateException, IOException {
        Criteria criteria = Criteria.where("metadata.lectureId").is(id);
        GridFSFile file = gridFsTemplate.findOne(new Query(criteria));

        Video video = new Video();
        video.setTitle(file.getMetadata().get("title").toString());
        video.setLength(file.getLength());
        video.setChunkLength(file.getChunkSize());
        video.setContentType(file.getMetadata().get("_contentType").toString());
        video.setStream(gridFsOperations.getResource(file).getInputStream());
        return video;
    }

    public void deleteVideo(String id) {
        gridFsTemplate.delete(
                new Query(Criteria.where("_id").is(id))
        );
    }

    public ResponseEntity<?> handleStreamVideo(Video video, HttpServletRequest request) throws IOException {
        long rangeStart = 0;
        long chunkSize = video.getChunkLength();
        long rangeEnd = chunkSize;
        String range = request.getHeader("Range");
        if (range != null) {
            String[] ranges = range.split("-");
            rangeStart = Long.parseLong(ranges[0].substring(6));
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            } else {
                rangeEnd = rangeStart + chunkSize;
            }

        }

        long fileSize = video.getLength();
        rangeEnd = Math.min(rangeEnd, fileSize - 1);
        byte[] streamData = readByteRange(video.getStream(), rangeStart, rangeEnd);

//        response.setContentType(video.getContentType());
        HttpStatus httpStatus = HttpStatus.PARTIAL_CONTENT;
        if (rangeEnd >= fileSize) {
            httpStatus = HttpStatus.OK;
        }

        return ResponseEntity.status(httpStatus)
                .header("Content-Type", "video/mp4")
                .header("Accept", "video/mp4")
                .header("Accept-Ranges", "bytes")
                .header("Content-Range", "bytes " + rangeStart + "-" +
                        rangeEnd + "/" + fileSize)
                .body(streamData);
    }

    private byte[] readByteRange(InputStream is, long start, long end) throws IOException {
        byte[] data = is.readAllBytes();
        byte[] result = new byte[(int) (end - start) + 1];
        System.arraycopy(data, (int) start, result, 0, (int) (end - start) + 1);
        return result;
    }
}
