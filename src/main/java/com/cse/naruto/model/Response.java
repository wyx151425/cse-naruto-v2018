package com.cse.naruto.model;

import com.cse.naruto.util.StatusCode;

public class Response<T> {
    private int statusCode;
    private T data;

    public Response() {
        this.statusCode = StatusCode.SUCCESS;
        this.data = null;
    }

    public Response(int statusCode) {
        this.statusCode = statusCode;
        this.data = null;
    }

    public Response(int statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public Response(T data) {
        this.statusCode = StatusCode.SUCCESS;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

