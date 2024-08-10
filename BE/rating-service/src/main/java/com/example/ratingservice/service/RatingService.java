package com.example.ratingservice.service;

import com.example.ratingservice.dto.EditReviewRequest;
import com.example.ratingservice.dto.ReviewDto;
import com.example.ratingservice.entity.AvgRating;
import com.example.ratingservice.entity.Review;
import com.example.ratingservice.exception.SearchNotFoundException;
import com.example.ratingservice.exception.UserAlreadyReviewedException;
import com.example.ratingservice.repository.AvgRatingRepository;
import com.example.ratingservice.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class RatingService {
    private AvgRatingRepository avgRatingRepository;
    private UserService userService;
    private CourseService courseService;
    private ReviewRepository reviewRepository;

    public void createReview(ReviewDto reviewDto) {
        Long courseId = reviewDto.getCourseId();
        Long userId = reviewDto.getUserId();
        if (!userService.isValidUser(userId)) throw new SearchNotFoundException("User not found");
        if (!courseService.isValidCourse(courseId)) throw new SearchNotFoundException("Course not found");

        if (reviewRepository.existsByUserIdAndCourseId(userId, courseId)) throw new UserAlreadyReviewedException("User already reviewed");
        AvgRating avgRating;
        Optional<AvgRating> optionalAvgRating = avgRatingRepository.findById(courseId);
        if (optionalAvgRating.isEmpty()) {
            avgRating = new AvgRating();
            avgRating.setCourseId(courseId);
        } else {
            avgRating = optionalAvgRating.get();
        }
        Review review = new Review();
        review.setUserId(userId);
        review.setCourseId(courseId);
        review.setComment(reviewDto.getComment());
        review.setRating(reviewDto.getRating());
        avgRating.getReviews().add(review);
        double rating = calculateAvgRating(avgRating.getReviews());
        avgRating.setAvgRating(rating);
        courseService.setCourseRating(courseId, rating);
        avgRatingRepository.save(avgRating);
    }

    public void editReview(EditReviewRequest request, Long userId, Long courseId) {
        if (!userService.isValidUser(userId)) throw new SearchNotFoundException("User not found");
        if (!courseService.isValidCourse(courseId)) throw new SearchNotFoundException("Course not found");
        Optional<AvgRating> optionalAvgRating = avgRatingRepository.findById(courseId);
        if (optionalAvgRating.isEmpty()) throw new SearchNotFoundException("Course haven't reviewed yet");
        AvgRating avgRating = optionalAvgRating.get();
        Set<Review> reviews = avgRating.getReviews();
        for (Review review : reviews) {
            if (review.getUserId().equals(userId) && review.getCourseId().equals(courseId)) {
                review.setComment(request.getComment());
                if (review.getRating() != request.getRating()) {
                    review.setRating(request.getRating());
                    double newAverage = calculateAvgRating(reviews);
                    avgRating.setAvgRating(newAverage);
                    courseService.setCourseRating(courseId, newAverage);
                }
                break;
            }
        }
        avgRatingRepository.save(avgRating);
    }

    public void deleteReview(Long userId, Long courseId) {
        if (!userService.isValidUser(userId)) throw new SearchNotFoundException("User not found");
        if (!courseService.isValidCourse(courseId)) throw new SearchNotFoundException("Course not found");
        Optional<AvgRating> optionalAvgRating = avgRatingRepository.findById(courseId);
        if (optionalAvgRating.isEmpty()) throw new SearchNotFoundException("Course haven't reviewed yet");
        AvgRating avgRating = optionalAvgRating.get();
        Set<Review> reviews = avgRating.getReviews();
        for (Review review : reviews) {
            if (review.getUserId().equals(userId) && review.getCourseId().equals(courseId)) {
                reviews.remove(review);
                double newAverage = 0;
                if (reviews.size() != 0) {
                    newAverage = calculateAvgRating(reviews);
                }
                avgRating.setAvgRating(newAverage);
                courseService.setCourseRating(courseId, newAverage);
                break;
            }
        }
        avgRatingRepository.save(avgRating);
    }

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public double getAverage(Long courseId) {
        if (!courseService.isValidCourse(courseId)) throw new SearchNotFoundException("Course not found");
        Optional<AvgRating> optionalAvgRating = avgRatingRepository.findById(courseId);
        if (optionalAvgRating.isEmpty()) throw new SearchNotFoundException("Course haven't reviewed yet");
        AvgRating avgRating = optionalAvgRating.get();
        return avgRating.getAvgRating();
    }

    public List<Review> getAllByUserIdAndCourseId(Long userId, Long courseId) {
        return reviewRepository.findAllByUserIdAndCourseId(userId, courseId);
    }

    public List<Review> getAllByUserId(Long userId) {
        return reviewRepository.findAllByUserId(userId);
    }

    public List<Review> getAllByCourseId(Long courseId) {
        return reviewRepository.findAllByCourseId(courseId);
    }

    private double calculateAvgRating(Set<Review> reviews) {
        double result = 0;
        for (Review review : reviews) {
            result += review.getRating();
        }
        return result / reviews.size();
    }
}
