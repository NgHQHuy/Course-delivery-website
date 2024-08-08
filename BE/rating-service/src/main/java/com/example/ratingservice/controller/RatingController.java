package com.example.ratingservice.controller;

import com.example.ratingservice.dto.BaseResponse;
import com.example.ratingservice.dto.EditReviewRequest;
import com.example.ratingservice.dto.ReviewDto;
import com.example.ratingservice.entity.Review;
import com.example.ratingservice.service.RatingService;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/rating")
@AllArgsConstructor
public class RatingController {
    private RatingService ratingService;

    @PostMapping("create")
    public ResponseEntity<BaseResponse> createReview(@Valid @RequestBody ReviewDto reviewDto) {
        ratingService.createReview(reviewDto);
        return ResponseEntity.ok(new BaseResponse("Success"));
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReview(@RequestParam(value = "user", required = false) Long userId, @RequestParam(value = "course", required = false) Long courseId) {
        if (userId == null && courseId == null) return ResponseEntity.ok(ratingService.getAll());
        else if (userId == null) return ResponseEntity.ok(ratingService.getAllByCourseId(courseId));
        else if (courseId == null) return ResponseEntity.ok(ratingService.getAllByUserId(userId));
        return ResponseEntity.ok(ratingService.getAllByUserIdAndCourseId(userId, courseId));
    }

    @GetMapping("getAverage")
    public ResponseEntity<Map<String, Double>> getAverage(@RequestParam("course") Long courseId) {
        double average = ratingService.getAverage(courseId);
        Map<String, Double> response = new HashMap<>();
        response.put("average", average);
        return ResponseEntity.ok(response);
    }

    @PostMapping("update")
    public ResponseEntity<BaseResponse> editReview(@RequestParam(value = "user") Long userId, @RequestParam(value = "course") Long courseId, @Valid @RequestBody EditReviewRequest request) {
        ratingService.editReview(request, userId, courseId);
        return ResponseEntity.ok(new BaseResponse("Success"));
    }

    @DeleteMapping("delete")
    public ResponseEntity<BaseResponse> deleteReview(@RequestParam(value = "user") Long userId, @RequestParam(value = "course") Long courseId) {
        ratingService.deleteReview(userId, courseId);
        return ResponseEntity.ok(new BaseResponse("Success"));
    }
}
