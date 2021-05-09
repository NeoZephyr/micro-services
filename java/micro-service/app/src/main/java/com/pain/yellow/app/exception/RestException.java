package com.pain.yellow.app.exception;

import com.pain.yellow.response.ResponseStatus;

public class RestException extends RuntimeException {
    private ResponseStatus responseStatus;

    public RestException(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}
