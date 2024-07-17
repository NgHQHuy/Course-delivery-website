package com.example.registeredcourse.validate;

import com.example.registeredcourse.entity.UserCourse;
import com.example.registeredcourse.exception.BodyParameterMissingException;

public class UserCourseValidation {
    public static void validate(UserCourse uc) {
        if (uc.getCourseId() == null) throw new BodyParameterMissingException("Missing courseId");
        else if (uc.getUserId() == null) throw new BodyParameterMissingException("Missing userId");
    }
}
