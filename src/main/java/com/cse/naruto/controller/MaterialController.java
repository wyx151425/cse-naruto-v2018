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
import java.util.List;

/**
 * 物料数据CRUD API
 *
 * @author 王振琦
 * createAt 2018/10/16
 * updateAt 2018/11/29
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
            materialService.importMaterialList(file);
        } catch (InvalidFormatException | IOException e) {
            throw new NarutoException(e, StatusCode.FILE_RESOLVE_ERROR);
        }
        return new Response<>();
    }

    @PostMapping(value = "materials/export")
    public void actionExportMaterialListToExcel() throws IOException {
        Workbook workbook = materialService.exportMaterialList();
        getResponse().reset();
        getResponse().setHeader("Content-Disposition", "attachment;filename=data.xlsx");
        getResponse().setContentType("application/octet-stream");
        OutputStream out = getResponse().getOutputStream();
        BufferedOutputStream buffer = new BufferedOutputStream(out);
        buffer.flush();
        workbook.write(buffer);
        buffer.close();
    }

    @PutMapping(value = "materials/technology")
    public Response<Material> actionUpdateMaterialByTechnologyCenter(@RequestBody Material material) {
        materialService.updateMaterialTechnology(material);
        return new Response<>();
    }

    @PutMapping(value = "materials/quality")
    public Response<Material> actionUpdateMaterialByQualifiedEnvironment(@RequestBody Material material) {
        materialService.updateMaterialQuality(material);
        return new Response<>();
    }

    @PutMapping(value = "materials/purchase")
    public Response<Material> actionUpdateMaterialByPurchase(@RequestBody Material material) {
        materialService.updateMaterialPurchase(material);
        return new Response<>();
    }

    @PutMapping(value = "materials/assembly")
    public Response<Material> actionUpdateMaterialByAssemblyCenter(@RequestBody Material material) {
        materialService.updateMaterialAssembly(material);
        return new Response<>();
    }

    @PutMapping(value = "materials/produce")
    public Response<Material> actionUpdateMaterialByProduceOperate(@RequestBody Material material) {
        materialService.updateMaterialProduce(material);
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
            @RequestParam("exportStatus") Integer exportStatus,
            @RequestParam("perfectStatus") Integer perfectStatus
    ) {
        PageContext<Material> pageContext = materialService.findMaterialListByPagination(pageIndex, pageSize, exportStatus, perfectStatus, getCurrentUser());
        return new Response<>(pageContext);
    }

    @GetMapping(value = "materials/query")
    public Response<List<Material>> actionQueryMaterialListByCodeLike(
            @RequestParam("materialCode") String materialCode,
            @RequestParam("perfectStatus") Integer perfectStatus
    ) {
        List<Material> materialList = materialService.findMaterialListByCodeLike(materialCode, perfectStatus, getCurrentUser());
        return new Response<>(materialList);
    }

    @GetMapping(value = "materials/query2")
    public Response<List<Material>> actionQueryMaterialListByStructureNo(
            @RequestParam("structureNo") String structureNo,
            @RequestParam("perfectStatus") Integer perfectStatus
    ) {
        List<Material> materialList = materialService.findMaterialListByStructureNo(structureNo, perfectStatus, getCurrentUser());
        return new Response<>(materialList);
    }

    @GetMapping(value = "materials/query3")
    public Response<List<Material>> actionQueryMaterialListByResourceMark(
            @RequestParam("resourceMark") String resourceMark,
            @RequestParam("perfectStatus") Integer perfectStatus
    ) {
        List<Material> materialList = materialService.findMaterialListByResourceMark(resourceMark, perfectStatus, getCurrentUser());
        return new Response<>(materialList);
    }

    @PostMapping(value = "materials/perfect")
    public Response<Material> actionImportPerfectedMaterials(
            @RequestParam("file") MultipartFile file,
            @RequestParam("sheetName") String sheetName,
            @RequestParam("deptMark") Integer deptMark
    ) {
        if (!Constant.DocType.XLSX.equals(file.getContentType())) {
            throw new NarutoException(StatusCode.FILE_FORMAT_ERROR);
        }
        try {
            materialService.importPerfectedMaterialsByDepartment(file, sheetName, deptMark);
        } catch (InvalidFormatException | IOException e) {
            throw new NarutoException(e, StatusCode.FILE_RESOLVE_ERROR);
        }
        return new Response<>();
    }

    @GetMapping(value = "materials/imperfect")
    public void actionExportImperfectMaterials(@RequestParam("deptMark") Integer deptMark) throws IOException {
        Workbook workbook = materialService.exportImperfectMaterialsByDepartment(deptMark);
        getResponse().reset();
        getResponse().setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        getResponse().setContentType("application/octet-stream");
        OutputStream out = getResponse().getOutputStream();
        BufferedOutputStream buffer = new BufferedOutputStream(out);
        buffer.flush();
        workbook.write(buffer);
        buffer.close();
    }

}
