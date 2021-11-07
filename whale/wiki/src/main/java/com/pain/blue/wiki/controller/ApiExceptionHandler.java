package com.pain.blue.wiki.controller;

import com.pain.blue.exception.RestException;
import com.pain.blue.rest.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Objects;

// @RestControllerAdvice
@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public RestResponse validExceptionHandler(BindException e) {
        String defaultMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();

        log.warn("参数校验失败：{}", defaultMessage);

        return RestResponse.error(400, defaultMessage);
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    @ResponseBody
    public RestResponse uploadFileExceptionHandler(MaxUploadSizeExceededException ex) {
        return RestResponse.error(400, "upload size limit");
    }

    @ExceptionHandler(value = RestException.class)
    public RestResponse restExceptionHandler(RestException ex) {
        return RestResponse.error(ex.getResponseStatus());
    }

    // @ExceptionHandler
    // public RestResponse handle(HttpServletRequest req, HandlerMethod method, Exception ex) {
}