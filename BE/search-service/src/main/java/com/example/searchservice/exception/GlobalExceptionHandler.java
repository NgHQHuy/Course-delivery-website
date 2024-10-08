package com.example.searchservice.exception;

import com.example.searchservice.dto.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleException(Exception exception) {
        logger = LoggerFactory.getLogger(exception.getClass());

        logger.error(exception.getMessage());

        ResponseEntity<BaseResponse> response = null;

        if (exception instanceof SearchNotFoundException) {
            response = new ResponseEntity<>(new BaseResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
//        else if (exception instanceof HttpMessageNotReadableException) {
//            response = new ResponseEntity<>(new BaseResponse("Missing request body or body is not format correctly"), HttpStatus.INTERNAL_SERVER_ERROR);
//        } else if (exception instanceof BodyParameterMissingException || exception instanceof UserAlreadyRegisteredException) {
//            response = new ResponseEntity<>(new BaseResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
//        } else if (exception instanceof MethodArgumentNotValidException) {
//            String message = ((MethodArgumentNotValidException) exception).getAllErrors().get(0).getDefaultMessage();
//            response = new ResponseEntity<>(new BaseResponse(message), HttpStatus.BAD_REQUEST);
//        } else if (exception instanceof AuthErrorException) {
//            response = new ResponseEntity<>(new BaseResponse(exception.getMessage()), HttpStatus.UNAUTHORIZED);
//        }

        if (response == null) {
            response = new ResponseEntity<>(new BaseResponse("An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }
}
