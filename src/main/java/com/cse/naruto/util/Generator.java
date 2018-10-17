package com.cse.naruto.util;

import java.util.UUID;

/**
 * 特殊字符串生成器
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/09/19
 */
public class Generator {

    public static String getObjectId() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
