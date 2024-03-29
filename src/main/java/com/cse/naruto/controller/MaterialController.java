package com.cse.naruto.controller;

import com.cse.naruto.context.exception.NarutoException;
import com.cse.naruto.model.*;
import com.cse.naruto.service.MaterialService;
import com.cse.naruto.util.Constant;
import com.cse.naruto.util.StatusCode;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
    public Response<List<ImportResult>> actionImportMaterialListByBOM(@RequestParam(value = "file") MultipartFile file) {
        if (!Constant.DocType.XLSX.equals(file.getContentType())) {
            throw new NarutoException(StatusCode.FILE_FORMAT_ERROR);
        }
        List<ImportResult> resultList;
        try {
            resultList = materialService.importMaterialList(file, getCurrentUser());
        } catch (InvalidFormatException | IOException e) {
            throw new NarutoException(e, StatusCode.FILE_RESOLVE_ERROR);
        }
        return new Response<>(resultList);
    }

    @PostMapping(value = "materials/guaranteeImport")
    public Response<Object> actionImportSCMaterialList(@RequestParam(value = "file") MultipartFile file) {
        if (!Constant.DocType.XLSX.equals(file.getContentType())) {
            throw new NarutoException(StatusCode.FILE_FORMAT_ERROR);
        }
        try {
            materialService.importGuaranteeMaterialList(file, getCurrentUser());
        } catch (InvalidFormatException | IOException e) {
            throw new NarutoException(e, StatusCode.FILE_RESOLVE_ERROR);
        }
        return new Response<>();
    }

    @PostMapping(value = "materials/importpbom")
    public Response<List<ImportResult>> actionImportPBOM(@RequestParam(value = "file") MultipartFile file) {
        if (!Constant.DocType.XLSX.equals(file.getContentType())) {
            throw new NarutoException(StatusCode.FILE_FORMAT_ERROR);
        }
        List<ImportResult> resultList;
        try {
            resultList = materialService.importPBOM(file, getCurrentUser());
        } catch (InvalidFormatException | IOException e) {
            throw new NarutoException(e, StatusCode.FILE_RESOLVE_ERROR);
        }
        return new Response<>(resultList);
    }

    @PostMapping(value = "materials/perfectpbom")
    public void actionPerfectPBOM(@RequestParam(value = "file") MultipartFile file) throws IOException {
        if (!Constant.DocType.XLSX.equals(file.getContentType())) {
            throw new NarutoException(StatusCode.FILE_FORMAT_ERROR);
        }
        XSSFWorkbook workbook;
        try {
            workbook = materialService.perfectPBOM(file);
        } catch (InvalidFormatException | IOException e) {
            throw new NarutoException(e, StatusCode.FILE_RESOLVE_ERROR);
        }
        getResponse().reset();
        getResponse().setContentType("application/octet-stream");
        OutputStream out = getResponse().getOutputStream();
        BufferedOutputStream buffer = new BufferedOutputStream(out);
        buffer.flush();
        workbook.write(buffer);
        buffer.close();
    }

    @PostMapping(value = "materials/import/structure")
    public Response<Material> actionImportStructureInfoByBOM(@RequestParam(value = "file") MultipartFile file) {
        if (!Constant.DocType.XLSX.equals(file.getContentType())) {
            throw new NarutoException(StatusCode.FILE_FORMAT_ERROR);
        }
        try {
            materialService.importStructureInfo(file);
        } catch (InvalidFormatException | IOException e) {
            throw new NarutoException(e, StatusCode.FILE_RESOLVE_ERROR);
        }
        return new Response<>();
    }

    @PostMapping(value = "materials/import/origin")
    public Response<Material> actionImportOriginMaterialListByBOM(@RequestParam(value = "file") MultipartFile file) {
        if (!Constant.DocType.XLSX.equals(file.getContentType())) {
            throw new NarutoException(StatusCode.FILE_FORMAT_ERROR);
        }
        try {
            materialService.importOriginMaterialList(file);
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

    @PutMapping(value = "materials")
    public Response<Material> actionUpdateMaterial(@RequestBody Material material) {
        materialService.updateMaterial(material);
        return new Response<>();
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

    @PutMapping(value = "materials/purchaseAll")
    public Response<Material> actionUpdateMaterialByPurchaseAll(@RequestBody List<Material> materialList) {
        for (Material material : materialList) {
            materialService.updateMaterialPurchase(material);
        }
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

    @PutMapping(value = "materials/confirmOne")
    public Response<Material> actionConfirmGuaranteeMaterial(@RequestBody Material material) {
        List<Material> materialList = new ArrayList<>();
        materialList.add(material);
        materialService.confirmGuaranteeMaterialList(materialList);
        return new Response<>();
    }

    @PutMapping(value = "materials/confirmAll")
    public Response<Material> actionConfirmGuaranteeAllMaterial(@RequestBody List<Material> materialList) {
        materialService.confirmGuaranteeMaterialList(materialList);
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

    @GetMapping(value = "materials/guarantee")
    public Response<PageContext<Material>> actionQueryGuaranteeMaterialListByPagination(
            @RequestParam("pageIndex") Integer pageIndex,
            @RequestParam("pageSize") Integer pageSize
    ) {
        PageContext<Material> pageContext = materialService.findGuaranteeMaterialListByPagination(pageIndex, pageSize, getCurrentUser());
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

    @PostMapping(value = "materials/export/dept")
    public void actionExportImperfectMaterials(
            @RequestParam("deptMark") Integer deptMark,
            @RequestParam("perfectStatus") Integer perfectStatus
    ) throws IOException {
        Workbook workbook = materialService.exportMaterialsByDepartment(deptMark, perfectStatus);
        getResponse().reset();
        getResponse().setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        getResponse().setContentType("application/octet-stream");
        OutputStream out = getResponse().getOutputStream();
        BufferedOutputStream buffer = new BufferedOutputStream(out);
        buffer.flush();
        workbook.write(buffer);
        buffer.close();
    }

    @PostMapping(value = "materials/export/all")
    public void actionExportAllMaterials() throws IOException {
        Workbook workbook = materialService.exportAll();
        getResponse().reset();
        getResponse().setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        getResponse().setContentType("application/octet-stream");
        OutputStream out = getResponse().getOutputStream();
        BufferedOutputStream buffer = new BufferedOutputStream(out);
        buffer.flush();
        workbook.write(buffer);
        buffer.close();
    }

    @GetMapping(value = "materials/count")
    public Response<Perfect> actionStatisticCount() {
        Perfect perfect = materialService.statisticCount();
        return new Response<>(perfect);
    }
}
