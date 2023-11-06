package com.example.newbst.utils;

import org.springframework.stereotype.Component;

@Component
public class ApiResponse {
    private Object data;
    private String token;

    public void setData(Object data) {
        this.data = data;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getData() {
        return data;
    }

    public String getToken() {
        return token;
    }
}
