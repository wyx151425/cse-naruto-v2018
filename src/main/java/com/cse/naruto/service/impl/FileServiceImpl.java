package com.cse.naruto.service.impl;

import com.cse.naruto.context.exception.NarutoException;
import com.cse.naruto.model.Material;
import com.cse.naruto.repository.MaterialRepository;
import com.cse.naruto.service.FileService;
import com.cse.naruto.util.Constant;
import com.cse.naruto.util.Generator;
import com.cse.naruto.util.StatusCode;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件导入业务逻辑实现类
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/10/15
 */
@Service
public class FileServiceImpl implements FileService {

    private final MaterialRepository materialRepository;

    @Autowired
    public FileServiceImpl(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    private Material getNewMaterial() {
        Material material = new Material();
        material.setObjectId(Generator.getObjectId());
        material.setStatus(Constant.MaterialStatus.COMMON);
        LocalDateTime dateTime = LocalDateTime.now().withNano(0);
        material.setCreateAt(dateTime);
        material.setUpdateAt(dateTime);
        material.setCompanyNo("01");
        material.setCode("");
        material.setName("");
        material.setShortName("");
        material.setSpecification("");
        material.setModel("");
        material.setDrawingNo("");
        material.setGeneralSort("");
        material.setInventoryUnit("");
        material.setSourceMark("");
        material.setRespDept("");
        material.setRespCompany("");
        material.setKeyPartMark("");
        material.setKeyPartSort("");
        material.setVirtualPartMark("N");
        material.setQualifiedMark("");
        material.setInspectMark("");
        material.setBatchMark("");
        material.setPurchaseSort("");
        material.setPurchaseMark("");
        material.setGroupPurMark("");
        material.setOwnPurMark("");
        material.setDefRepository("");
        material.setOutSource("N");
        material.setPlanner("");
        material.setFixedAdvTime("");
        return material;
    }

    @Override
    public void importCseBom(MultipartFile file) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheet("整机BOM");
        List<Material> materialList = new ArrayList<>(128);
        List<String> codeList = materialRepository.findAllMaterialCode();

        // Workbook行索引
        int index = 0;
        for (Row row : sheet) {
            // 前三行数据为机器信息及字段的批注，所以不予解析
            if (index < 3) {
                index++;
            } else {
                if (null == row.getCell(3)) {
                    break;
                }
                Material material = getNewMaterial();
                String code = row.getCell(3).toString();
                if (!codeList.contains(code)) {
                    material.setCode(code);
                    material.setName(row.getCell(7).toString());
                    material.setShortName("");
                    materialList.add(material);
                }
            }
        }

        materialRepository.saveAll(materialList);
    }
}
