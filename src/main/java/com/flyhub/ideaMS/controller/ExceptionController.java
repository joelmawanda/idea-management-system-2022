package com.flyhub.ideaMS.controller;

import com.flyhub.ideaMS.exception.IdeaMSExceptionResponse;
import com.flyhub.ideaMS.exception.Violation;
import javax.validation.ConstraintViolationException;

import com.flyhub.ideaMS.exception.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 *
 * @author Joel Mawanda
 * @author Jean Kekirunga
 */
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<IdeaMSExceptionResponse> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        IdeaMSExceptionResponse error = new IdeaMSExceptionResponse();
        error.setOperationResult(400);
        error.setOperationDescription("All fields must be filled correctly");

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            error.getViolations().add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        });
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<IdeaMSExceptionResponse> onConstraintValidationException(ConstraintViolationException e) {

        IdeaMSExceptionResponse error = new IdeaMSExceptionResponse();
        error.setOperationResult(400);
        error.setOperationDescription("All fields must be filled");

        e.getConstraintViolations().forEach(violation -> {
            error.getViolations().add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        });
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<IdeaMSExceptionResponse> onMissingServletRequestParameterException(MissingServletRequestParameterException e) {

        IdeaMSExceptionResponse error = new IdeaMSExceptionResponse();
        error.setOperationResult(400);
        error.setOperationDescription(e.getMessage());
        error.setRequestParameterName(e.getParameterName());
        error.setRequestParameterType(e.getParameterType());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<IdeaMSExceptionResponse> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        IdeaMSExceptionResponse error = new IdeaMSExceptionResponse();
        error.setOperationResult(405);
        error.setOperationDescription("Request method " + e.getMethod() +  " is not supported please use "
                + e.getSupportedHttpMethods());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("File too large!"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<IdeaMSExceptionResponse> handleUnKnownException(Exception ex){
        IdeaMSExceptionResponse error = new IdeaMSExceptionResponse();
        error.setOperationResult(500);
        error.setOperationDescription("Internal Server Error");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
