package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.exception.EmptyDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RestController
public class ExceptionHandlerController {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleAllException(HttpServletRequest request, Exception e) {
        LOG.error(e.getMessage(), e);
        Response responseObject = Response.builder()
                .code(Constant.FAILED_CODE)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Response> handleHttpMediaTypeNotSupported(HttpServletRequest request, Exception e) {
        LOG.error(e.getMessage());
        Response responseObject = Response.builder()
                .code(Constant.FAILED_CODE)
                .message("Invalid Format")
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> handleHttpMessageNotReadable(HttpServletRequest request, Exception e) {
        LOG.error(e.getMessage());
        Response responseObject = Response.builder()
                .code(Constant.FAILED_CODE)
                .message("http message not readable")
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(EmptyDataException.class)
    public ResponseEntity<Response> handleEmptyDataExceptionException(EmptyDataException e) {
        Response responseObject = Response.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .data(e.getData())
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @ExceptionHandler(ApiServiceException.class)
    public ResponseEntity<Response> handleEmptyDataExceptionException(ApiServiceException e) {
        Response responseObject = Response.builder()
                .code(Constant.FAILED_CODE)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }


}
