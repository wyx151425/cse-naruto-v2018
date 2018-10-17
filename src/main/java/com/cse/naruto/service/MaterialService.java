package com.cse.naruto.service;

import com.cse.naruto.model.Material;

/**
 * 物料业务逻辑
 *
 * @author 王振琦
 * createAt 2018/10/16
 * updateAt 2018/10/16
 */
public interface MaterialService {

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
}
