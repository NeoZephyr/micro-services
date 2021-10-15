package com.pain.blue.wiki.controller;

import com.pain.blue.rest.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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

    // @ExceptionHandler
    // public APIResponse handle(HttpServletRequest req, HandlerMethod method, Exception ex) {
}