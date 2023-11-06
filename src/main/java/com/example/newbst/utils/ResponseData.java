package com.example.newbst.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData {
    private int code;
    private boolean success;
    private String message;
    private Object data;



    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ResponseData success(String message, Object data) {
        ResponseData response = new ResponseData();
        response.setCode(200);
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static ResponseData failure(String message) {
        ResponseData response = new ResponseData();
        response.setCode(400);
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        return response;
    }
}
