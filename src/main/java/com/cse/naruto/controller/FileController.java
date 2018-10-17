package com.cse.naruto.controller;

import com.cse.naruto.context.exception.NarutoException;
import com.cse.naruto.model.Response;
import com.cse.naruto.service.FileService;
import com.cse.naruto.util.Constant;
import com.cse.naruto.util.StatusCode;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件导入导出控制器
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/09/19
 */
@RestController
@RequestMapping(value = "api/files")
public class FileController extends NarutoFacade {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("import/bom/cse")
    public Response<Object> actionImportCseBom(@RequestParam(value = "file") MultipartFile file) {
        if (!Constant.DocType.XLSX.equals(file.getContentType())) {
            throw new NarutoException(StatusCode.FILE_FORMAT_ERROR);
        }
        try {
            fileService.importCseBom(file);
        } catch (InvalidFormatException | IOException e) {
            throw new NarutoException(e, StatusCode.FILE_RESOLVE_ERROR);
        }
        return new Response<>();
    }
}
