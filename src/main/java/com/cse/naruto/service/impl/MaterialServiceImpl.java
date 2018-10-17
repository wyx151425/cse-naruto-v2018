package com.cse.naruto.service.impl;

import com.cse.naruto.model.Material;
import com.cse.naruto.repository.MaterialRepository;
import com.cse.naruto.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 物料业务逻辑
 *
 * @author 王振琦
 * createAt 2018/10/16
 * updateAt 2018/10/16
 */
@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialByTechnologyCenter(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setSpecification(material.getSpecification());
        targetMater.setModel(material.getModel());
        targetMater.setDescription(material.getDescription());
        targetMater.setDrawingNo(material.getDrawingNo());
        targetMater.setGeneralSort(material.getGeneralSort());
        targetMater.setInventoryUnit(material.getInventoryUnit());
        targetMater.setSourceMark(material.getSourceMark());
        targetMater.setRespCompany(material.getRespCompany());
        targetMater.setRespDept(material.getRespDept());
        targetMater.setKeyPartMark(material.getKeyPartMark());
        targetMater.setKeyPartSort(material.getKeyPartSort());
        targetMater.setVirtualPartMark(material.getVirtualPartMark());
        targetMater.setQualifiedMark(material.getQualifiedMark());
        targetMater.setUpdateAt(LocalDateTime.now().withNano(0));
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialByQualifiedEnvironment(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setQualifiedMark(material.getQualifiedMark());
        targetMater.setBatchMark(material.getBatchMark());
        targetMater.setUpdateAt(LocalDateTime.now().withNano(0));
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialByPurchase(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setPurchaseSort(material.getPurchaseSort());
        targetMater.setPurchaseMark(material.getPurchaseMark());
        targetMater.setGroupPurMark(material.getGroupPurMark());
        targetMater.setOwnPurMark(material.getOwnPurMark());
        targetMater.setUpdateAt(LocalDateTime.now().withNano(0));
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialByAssemblyCenter(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setDefRepository(material.getDefRepository());
        targetMater.setUpdateAt(LocalDateTime.now().withNano(0));
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialByProduceOperate(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setOutSource(material.getOutSource());
        targetMater.setPlanner(material.getPlanner());
        targetMater.setFixedAdvTime(material.getFixedAdvTime());
        targetMater.setUpdateAt(LocalDateTime.now().withNano(0));
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Material findMaterialByCode(String code) {
        return materialRepository.findMaterialByCode(code);
    }
}
