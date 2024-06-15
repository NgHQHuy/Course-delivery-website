package com.example.video.controller;

import com.example.video.dto.BasicDto;
import com.example.video.dto.ResponseDto;
import com.example.video.dto.UploadDto;
import com.example.video.entity.Video;
import com.example.video.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
public class VideoController {

    @Autowired
    private VideoService service;

    @Operation(summary = "Upload video")
    @PostMapping("/videos/add")
    public ResponseEntity<BasicDto> addVideos(@Parameter(description = "Tựa đề của video")
            @RequestParam("title") String title, @RequestBody(description = "File video. VD dưới bị sai. File phải được up dưới dạng Multipart") @RequestParam("file")MultipartFile file) {
        try {
            String id = service.addVideo(title, file);
            UploadDto uploadDto = new UploadDto(id);
            return ResponseEntity.ok(uploadDto);
        } catch (IOException e) {
            ResponseDto errorDto = new ResponseDto(-1, e.getMessage());
            return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Phát video")
    @CrossOrigin
    @GetMapping("/videos/stream/{id}.mp4")
    public ResponseEntity<?> streamVideo(@Parameter(description = "ID của video")
            @PathVariable("id") String id, HttpServletRequest request) throws IOException {
        Video video = service.getVideo(id);
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
//        response.setStatus(206);
//        FileCopyUtils.copy(video.getStream(), response.getOutputStream());
    }

    // TODO: làm api xóa video.
    @DeleteMapping("video/delete/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable String id) {
        service.deleteVideo(id);
        return ResponseEntity.ok(new ResponseDto(0, "Success"));
    }

    private byte[] readByteRange(InputStream is, long start, long end) throws IOException {
        byte[] data = is.readAllBytes();
        byte[] result = new byte[(int) (end - start) + 1];
        System.arraycopy(data, (int) start, result, 0, (int) (end - start) + 1);
        return result;
    }



    @ExceptionHandler({
            IllegalStateException.class,
            IOException.class,
            HttpMessageNotWritableException.class
    })
    public ResponseEntity<ResponseDto> handle(Exception e) {
        System.out.println(e.getMessage());
        ResponseDto errorDto = new ResponseDto(-1, e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
