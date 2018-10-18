package com.cse.naruto.util;

/**
 * 响应状态码
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/09/20
 */
public class StatusCode {
    public static final int SUCCESS = 200;
    public static final int SYSTEM_ERROR = 500;
    public static final int USER_NOT_EXIST = 1000;
    public static final int USER_REGISTERED = 1001;
    public static final int USER_DISABLED = 1002;
    public static final int USER_LOGIN_TIMEOUT = 1003;
    public static final int USER_LOGIN_PASSWORD_ERROR = 1004;
    public static final int FILE_FORMAT_ERROR = 1100;
    public static final int FILE_RESOLVE_ERROR = 1101;

}
