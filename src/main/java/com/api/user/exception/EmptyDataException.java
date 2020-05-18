package com.api.user.exception;

import com.api.user.define.Constant;

public class EmptyDataException extends Exception {
    private String message;
    private Object data;
    private int code;

    public EmptyDataException(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public EmptyDataException(String message, Object data) {
        this.code = Constant.SUCCESS_CODE;
        this.message = message;
        this.data = data;
    }

    public EmptyDataException() {
        this.code = Constant.SUCCESS_CODE;
        this.message = Constant.SUCCESS_MESSAGE;
        this.data = new String[0];
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
