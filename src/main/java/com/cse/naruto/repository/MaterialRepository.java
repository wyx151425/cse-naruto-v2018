package com.cse.naruto.repository;

import com.cse.naruto.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 物料数据DAO层
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/10/15
 */
public interface MaterialRepository extends JpaRepository<Material, Integer>, JpaSpecificationExecutor<Material> {

    /**
     * 根据物料编码获得物料对象
     *
     * @param code 物料编码
     * @return 物料对象
     */
    Material findMaterialByCode(String code);

    /**
     * 获取所有的物料编码
     *
     * @return 物料编码集合
     */
    @Query(value = "select m.code from Material m")
    List<String> findAllMaterialCode();

    /**
     * 根据状态码获取物料基础数据对象集合
     *
     * @param status 状态码
     * @return 物料基础数据对象集合
     */
    List<Material> findAllByExportStatus(int status);

    /**
     * 根据物料编码模糊查询物料数据
     *
     * @param materialCode  模糊查询匹配字符串
     * @param perfectStatus 完善状态
     * @return 物料数据集合
     */
    List<Material> findAllByCodeLikeAndTechnologyStatus(String materialCode, Integer perfectStatus);

    /**
     * 根据部套号查询物料
     *
     * @param structureNo   部套号
     * @param perfectStatus 完善状态
     * @return 物料数据集合
     */
    List<Material> findAllByStructureNoAndTechnologyStatus(String structureNo, Integer perfectStatus);

    /**
     * 根据货源标记查询无聊
     *
     * @param resourceMark  货源标记
     * @param perfectStatus 完善状态
     * @return 物料数据集合
     */
    List<Material> findAllByResourceMarkAndTechnologyStatus(String resourceMark, Integer perfectStatus);

    /**
     * 根据物料编码模糊查询物料数据
     *
     * @param materialCode 模糊查询匹配字符串
     * @return 物料数据集合
     */
    @Query(value = "select m from Material m where m.code like :materialCode")
    List<Material> findAllByCodeLike(@Param("materialCode") String materialCode);

    /**
     * 根据部套号查询物料
     *
     * @param structureNo 部套号
     * @return 物料数据集合
     */
    List<Material> findAllByStructureNo(String structureNo);

    /**
     * 根据货源标记查询无聊
     *
     * @param resourceMark 货源标记
     * @return 物料数据集合
     */
    List<Material> findAllByResourceMark(String resourceMark);
}
