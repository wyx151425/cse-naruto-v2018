package com.cse.naruto.model;

public class ImportResult {
    private int order;
    private String message;

    public ImportResult(int order, String message) {
        this.order = order;
        this.message = message;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ImportResult{" +
                "order=" + order +
                ", message='" + message + '\'' +
                '}';
    }
}
