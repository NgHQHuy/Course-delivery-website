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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private VideoService service;

    @Operation(summary = "Upload video")
    @PostMapping("/videos/add")
    public ResponseEntity<BasicDto> addVideos(@Parameter(description = "Tựa đề của video")
            @RequestParam("title") String title, @RequestParam("lectureId") Long lectureId, @RequestBody(description = "File video. VD dưới bị sai. File phải được up dưới dạng Multipart") @RequestParam("file")MultipartFile file) {
        try {
            String id = service.addVideo(title, file, lectureId);
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
        try {
            return service.handleStreamVideo(video, request);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
//        response.setStatus(206);
//        FileCopyUtils.copy(video.getStream(), response.getOutputStream());
    }

    @CrossOrigin
    @GetMapping("/videos/stream/lecture/{id}.mp4")
    public ResponseEntity<?> streamVideoWithLectureId(@Parameter(description = "ID của video")
                                         @PathVariable("id") Long id, HttpServletRequest request) throws IOException {
        Video video = service.getVideoWithLectureId(id);
        try {
            return service.handleStreamVideo(video, request);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
//        response.setStatus(206);
//        FileCopyUtils.copy(video.getStream(), response.getOutputStream());
    }

    // TODO: làm api xóa video.
    @DeleteMapping("video/delete/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable String id) {
        service.deleteVideo(id);
        return ResponseEntity.ok(new ResponseDto(0, "Success"));
    }
}
