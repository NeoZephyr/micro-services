package com.pain.yellow.app.exception;

import com.pain.yellow.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestException.class)
    @ResponseBody
    public RestResponse<String> resolve(RestException ex) {
        log.error("resolve in rest exception handler", ex);
        return RestResponse.error(ex.getResponseStatus());
    }
}
