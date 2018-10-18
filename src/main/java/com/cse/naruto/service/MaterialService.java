package com.cse.naruto.service;

import com.cse.naruto.model.Material;
import com.cse.naruto.model.PageContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.IOException;

/**
 * 物料业务逻辑
 *
 * @author 王振琦
 * createAt 2018/10/16
 * updateAt 2018/10/18
 */
public interface MaterialService {

    /**
     * 通过BOM导入物料
     *
     * @param file 文件
     * @throws IOException            输入输出异常
     * @throws InvalidFormatException 文件格式错误异常
     */
    void saveMaterialListByBOM(MultipartFile file) throws IOException, InvalidFormatException;

    /**
     * 导出物料基础数据到EXCEL表中
     * @return 处理完成的EXCEL表
     */
    Workbook findMaterialListToExcel();

    /**
     * 技术中心更新物料基础数据的方法
     *
     * @param material 包含物料信息的物料对象
     */
    void updateMaterialByTechnologyCenter(Material material);

    /**
     * 质量环保部更新物料基础数据的方法
     *
     * @param material 包含物料信息的物料对象
     */
    void updateMaterialByQualifiedEnvironment(Material material);

    /**
     * 采购部更新物料基础数据的方法
     *
     * @param material 包含物料信息的物料对象
     */
    void updateMaterialByPurchase(Material material);

    /**
     * 集配中心更新物料基础数据的方法
     *
     * @param material 包含物料信息的物料对象
     */
    void updateMaterialByAssemblyCenter(Material material);

    /**
     * 生产运行部更新物料基础数据的方法
     *
     * @param material 包含物料信息的物料对象
     */
    void updateMaterialByProduceOperate(Material material);

    /**
     * 根据物料编码获取物料对象
     *
     * @param code 物料编码
     * @return 物料对象
     */
    Material findMaterialByCode(String code);

    /**
     * 分页查询物料数据集合
     *
     * @param pageIndex 分页页码
     * @param pageSize  每页数据量
     * @param status    查询等于该状态码的数据
     * @return 带有分页信息和物料数据集合的对象
     */
    PageContext<Material> findMaterialListByPagination(int pageIndex, int pageSize, int status);
}
