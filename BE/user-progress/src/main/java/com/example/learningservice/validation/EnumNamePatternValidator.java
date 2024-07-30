package com.example.learningservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EnumNamePatternValidator implements ConstraintValidator<EnumNamePattern, Enum<?>> {
    private Pattern pattern;

    @Override
    public void initialize(EnumNamePattern constraintAnnotation) {
        try {
            pattern = Pattern.compile(constraintAnnotation.regexp());
        } catch (PatternSyntaxException ex) {
            throw new IllegalStateException("Given regex is invalid", ex);
        }
    }

    @Override
    public boolean isValid(Enum<?> anEnum, ConstraintValidatorContext constraintValidatorContext) {
        if (anEnum == null) return true;

        Matcher m = pattern.matcher(anEnum.name());
        return m.matches();
    }


}
