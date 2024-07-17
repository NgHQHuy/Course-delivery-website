package com.example.userprogress.validation;

import com.example.userprogress.entity.UserProgress;
import com.example.userprogress.exception.BodyParameterMissingException;

public class UserProgressValidation {
    public static void validate(UserProgress userProgress) {
        if (userProgress.getUserId() == null) {
            throw new BodyParameterMissingException("Missing userId");
        } else if (userProgress.getSectionId() == null) {
            throw new BodyParameterMissingException("Missing sectionId");
        } else if (userProgress.getCourseId() == null) {
            throw new BodyParameterMissingException("Missing courseId");
        } else if (userProgress.getStatus() == null) {
            throw new BodyParameterMissingException("Missing status");
        } else if (userProgress.getLectureId() == null) {
            throw new BodyParameterMissingException("Missing lectureId");
        } else if (userProgress.getTimestamp()  < 0) {
            throw new BodyParameterMissingException("Missing timestamp or not appropriate");
        }
    }
}