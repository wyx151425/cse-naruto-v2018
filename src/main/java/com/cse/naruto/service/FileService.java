package com.cse.naruto.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件导入业务逻辑
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/09/19
 */
public interface FileService {

    /**
     * 导入CSE BOM文件
     *
     * @param file CSE BOM文件
     * @throws IOException            文件读入异常
     * @throws InvalidFormatException 文件格式错误异常
     */
    void importCseBom(MultipartFile file) throws IOException, InvalidFormatException;
}
