package com.cse.naruto.service;

import com.cse.naruto.model.Material;
import com.cse.naruto.model.PageContext;
import com.cse.naruto.model.User;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;

/**
 * 物料业务逻辑
 *
 * @author 王振琦
 * createAt 2018/10/16
 * updateAt 2018/11/29
 */
public interface MaterialService {

    /**
     * 通过BOM导入物料基础数据
     *
     * @param file BOM文件
     * @throws IOException            输入输出异常
     * @throws InvalidFormatException 文件格式错误异常
     */
    void importMaterialList(MultipartFile file) throws IOException, InvalidFormatException;

    /**
     * 导出物料基础数据
     *
     * @return 处理完成的EXCEL表
     */
    Workbook exportMaterialList();

    /**
     * 更新物料基础数据的技术信息
     *
     * @param material 包含物料信息的物料对象
     */
    void updateMaterialTechnology(Material material);

    /**
     * 更新物料基础数据的质量信息
     *
     * @param material 包含物料信息的物料对象
     */
    void updateMaterialQuality(Material material);

    /**
     * 更新物料基础数据的采购信息
     *
     * @param material 包含物料信息的物料对象
     */
    void updateMaterialPurchase(Material material);

    /**
     * 更新物料基础数据的装配信息
     *
     * @param material 包含物料信息的物料对象
     */
    void updateMaterialAssembly(Material material);

    /**
     * 更新物料基础数据的生产信息
     *
     * @param material 包含物料信息的物料对象
     */
    void updateMaterialProduce(Material material);

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
     * @param pageIndex     分页页码
     * @param pageSize      每页数据量
     * @param exportStatus  查询等于该状态码的数据（0-待完善/1-可导出/2-已导出/3-全部）
     * @param perfectStatus 完善状态（0-未完善/1-已完善/2-全部）
     * @param user          当前用户
     * @return 带有分页信息和物料数据集合的对象
     */
    PageContext<Material> findMaterialListByPagination(int pageIndex, int pageSize, int exportStatus, int perfectStatus, User user);

    /**
     * 模糊查询物料
     *
     * @param materialCode 查询字符串
     * @return 物料数据集合
     */
    List<Material> findMaterialListByCodeLike(String materialCode);
}
