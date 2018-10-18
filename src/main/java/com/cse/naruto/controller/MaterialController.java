package com.cse.naruto.controller;

import com.cse.naruto.context.exception.NarutoException;
import com.cse.naruto.model.Material;
import com.cse.naruto.model.PageContext;
import com.cse.naruto.model.Response;
import com.cse.naruto.service.MaterialService;
import com.cse.naruto.util.Constant;
import com.cse.naruto.util.StatusCode;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 物料数据CRUD API
 *
 * @author 王振琦
 * createAt 2018/10/16
 * updateAt 2018/10/17
 */
@RestController
@RequestMapping(value = "api")
public class MaterialController extends NarutoFacade {

    private final MaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping(value = "materials/import")
    public Response<Material> actionImportMaterialListByBOM(@RequestParam(value = "file") MultipartFile file) {
        if (!Constant.DocType.XLSX.equals(file.getContentType())) {
            throw new NarutoException(StatusCode.FILE_FORMAT_ERROR);
        }
        try {
            materialService.saveMaterialListByBOM(file);
        } catch (InvalidFormatException | IOException e) {
            throw new NarutoException(e, StatusCode.FILE_RESOLVE_ERROR);
        }
        return new Response<>();
    }

    @PostMapping(value = "materials/export")
    public void actionExportMaterialListToExcel() throws IOException {
        Workbook workbook = materialService.findMaterialListToExcel();
        getResponse().reset();
        getResponse().setHeader("Content-Disposition", "attachment;filename=excel.xlsx");
        getResponse().setContentType("application/octet-stream");
        OutputStream out = getResponse().getOutputStream();
        BufferedOutputStream buffer = new BufferedOutputStream(out);
        buffer.flush();
        workbook.write(buffer);
        buffer.close();
    }

    @PutMapping(value = "materials/technologyCenter")
    public Response<Material> actionUpdateMaterialByTechnologyCenter(@RequestBody Material material) {
        materialService.updateMaterialByTechnologyCenter(material);
        return new Response<>();
    }

    @PutMapping(value = "materials/qualifiedEnvironment")
    public Response<Material> actionUpdateMaterialByQualifiedEnvironment(@RequestBody Material material) {
        materialService.updateMaterialByQualifiedEnvironment(material);
        return new Response<>();
    }

    @PutMapping(value = "materials/purchase")
    public Response<Material> actionUpdateMaterialByPurchase(@RequestBody Material material) {
        materialService.updateMaterialByPurchase(material);
        return new Response<>();
    }

    @PutMapping(value = "materials/assemblyCenter")
    public Response<Material> actionUpdateMaterialByAssemblyCenter(@RequestBody Material material) {
        materialService.updateMaterialByAssemblyCenter(material);
        return new Response<>();
    }

    @PutMapping(value = "materials/produceOperate")
    public Response<Material> actionUpdateMaterialByProduceOperate(@RequestBody Material material) {
        materialService.updateMaterialByProduceOperate(material);
        return new Response<>();
    }

    @GetMapping(value = "materials/{code}")
    public Response<Material> actionQueryMaterialByCode(@PathVariable(value = "code") String code) {
        Material material = materialService.findMaterialByCode(code);
        return new Response<>(material);
    }

    @GetMapping(value = "materials")
    public Response<PageContext<Material>> actionQueryMaterialListByPagination(
            @RequestParam("pageIndex") Integer pageIndex,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("status") Integer status
    ) {
        PageContext<Material> pageContext = materialService.findMaterialListByPagination(pageIndex, pageSize, status);
        return new Response<>(pageContext);
    }
}
