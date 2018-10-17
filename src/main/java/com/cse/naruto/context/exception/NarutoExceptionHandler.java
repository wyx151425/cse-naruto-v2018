package com.cse.naruto.context.exception;

import com.cse.naruto.model.Response;
import com.cse.naruto.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 应用异常处理器
 *
 * @author 王振琦
 * createAt 2018/09/20
 * updateAt 2018/09/20
 */
@RestControllerAdvice
public class NarutoExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(NarutoExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public Response<Object> handleException(Exception e) {
        logger.error("NarutoExceptionHandler: ", e);
        return new Response<>(StatusCode.SYSTEM_ERROR);
    }

    @ExceptionHandler(value = NarutoException.class)
    public Response<Object> handleSummerException(NarutoException e) {
        logger.error("NarutoExceptionHandler: ", e);
        return new Response<>(e.getStatusCode());
    }
}
