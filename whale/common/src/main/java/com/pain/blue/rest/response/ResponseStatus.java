package com.pain.blue.rest.response;

public class ResponseStatus {

    public static ResponseStatus SUCCESS = new ResponseStatus(0, "success");

    private final int code;
    private final String msg;

    public ResponseStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
