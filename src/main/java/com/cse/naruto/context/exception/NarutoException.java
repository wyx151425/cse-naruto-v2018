package com.cse.naruto.context.exception;

/**
 * 应用通用异常
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/09/19
 */
public class NarutoException extends RuntimeException {
    private int statusCode;

    public NarutoException(int statusCode) {
        this.statusCode = statusCode;
    }

    public NarutoException(Throwable cause, int statusCode) {
        super(cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
