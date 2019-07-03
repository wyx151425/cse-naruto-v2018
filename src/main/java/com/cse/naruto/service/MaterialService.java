package com.cse.naruto.service;

import com.cse.naruto.model.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
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
    List<ImportResult> importMaterialList(MultipartFile file, User user) throws IOException, InvalidFormatException;

    /**
     * 以标准格式导入服务保障中心的物料基础数据
     *
     * @param file BOM文件
     * @throws IOException            输入输出异常
     * @throws InvalidFormatException 文件格式错误异常
     */
    void importGuaranteeMaterialList(MultipartFile file, User user) throws IOException, InvalidFormatException;

    /**
     * 导入P-BOM中的数据
     *
     * @param file P-BOM文件
     * @throws IOException            输入输出异常
     * @throws InvalidFormatException 文件格式错误异常
     */
    List<ImportResult> importPBOM(MultipartFile file, User user) throws IOException, InvalidFormatException;

    /**
     * 完善PBOM
     *
     * @param file PBOM文件
     * @return 完善后的PBOM
     * @throws IOException            输入输出异常
     * @throws InvalidFormatException 文件格式错误异常
     */
    XSSFWorkbook perfectPBOM(MultipartFile file) throws IOException, InvalidFormatException;

    /**
     * 通过BOM导入物料基础数据
     *
     * @param file BOM文件
     * @throws IOException            输入输出异常
     * @throws InvalidFormatException 文件格式错误异常
     */
    void importOriginMaterialList(MultipartFile file) throws IOException, InvalidFormatException;

    /**
     * 导入部套信息
     *
     * @param file BOM文件
     * @throws IOException            输入输出异常
     * @throws InvalidFormatException 文件格式错误异常
     */
    void importStructureInfo(MultipartFile file) throws IOException, InvalidFormatException;

    /**
     * 导出需要通过Excel表编辑的物料基础数据
     *
     * @param deptMark      部门标记
     * @param perfectStatus 完善状态
     * @return 工作表
     */
    Workbook exportMaterialsByDepartment(Integer deptMark, Integer perfectStatus);

    /**
     * 导入修改完成的Excel
     *
     * @param file      BOM文件
     * @param sheetName 待解析的表的表名
     * @param deptMark  部门标记
     * @throws IOException            输入输出异常
     * @throws InvalidFormatException 文件格式错误异常
     */
    void importPerfectedMaterialsByDepartment(MultipartFile file, String sheetName, Integer deptMark) throws IOException, InvalidFormatException;

    /**
     * 导出物料基础数据
     *
     * @return 处理完成的EXCEL表
     */
    Workbook exportMaterialList();


    /**
     * 导出所有物料基础数据
     *
     * @return 处理完成的EXCEL表
     */
    Workbook exportAll();

    /**
     * 更新物料基础数据的技术信息
     *
     * @param material 包含物料信息的物料对象
     */
    void updateMaterial(Material material);

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
     * 分页查询服务保障中心的基础数据
     *
     * @param pageIndex 分页索引
     * @param pageSize  分页数据量
     * @param user      用户
     * @return 带有分页信息和物料数据集合的对象
     */
    PageContext<Material> findGuaranteeMaterialListByPagination(int pageIndex, int pageSize, User user);

    /**
     * 模糊查询物料
     *
     * @param materialCode  查询字符串
     * @param perfectStatus 完善状态
     * @param user          用户
     * @return 物料数据集合
     */
    List<Material> findMaterialListByCodeLike(String materialCode, Integer perfectStatus, User user);

    /**
     * 根据部套号查询物料
     *
     * @param structureNo   部套号
     * @param perfectStatus 完善状态
     * @param user          用户
     * @return 物料数据集合
     */
    List<Material> findMaterialListByStructureNo(String structureNo, Integer perfectStatus, User user);

    /**
     * 根据货源标记查询物料
     *
     * @param resourceMark  货源标记
     * @param perfectStatus 完善状态
     * @param user          用户
     * @return 物料数据集合
     */
    List<Material> findMaterialListByResourceMark(String resourceMark, Integer perfectStatus, User user);

    /**
     * 统计数据完善情况
     *
     * @return 完善情况对象
     */
    Perfect statisticCount();

    /**
     * 确认服务保障中心的物料
     * @param materialList
     */
    void confirmGuaranteeMaterialList(List<Material> materialList);
}
