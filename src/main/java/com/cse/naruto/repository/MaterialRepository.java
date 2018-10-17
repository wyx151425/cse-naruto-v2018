package com.cse.naruto.repository;

import com.cse.naruto.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 物料数据DAO层
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/10/15
 */
public interface MaterialRepository extends JpaRepository<Material, Integer> {

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
}
