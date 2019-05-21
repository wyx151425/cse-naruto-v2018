package com.cse.naruto.model;

import com.cse.naruto.util.Constant;
import com.cse.naruto.util.Generator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 物料类
 *
 * @author 王振琦
 * createAt 2018/09/18
 * updateAt 2018/10/15
 */
@Entity
@Table(name = "cse_material")
public class Material extends NarutoEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_material")
    @SequenceGenerator(name = "generator_material", sequenceName = "sequence_material", allocationSize = 1)
    private Integer id;
    /**
     * 公司号
     */
    private String companyNo;
    /**
     * 部套号
     */
    private String structureNo;
    /**
     * 物料编码
     */
    private String code;
    /**
     * 兼容旧编码
     */
    private String originCode;
    /**
     * 物料名称
     */
    private String name;
    /**
     * 规格
     */
    private String specification;
    /**
     * 型号
     */
    private String model;
    /**
     * 具体描述
     */
    private String description;
    /**
     * 国标号
     */
    private String international;
    /**
     * 图号
     */
    private String drawingNo;
    /**
     * 参考号
     */
    private String reference;
    /**
     * 普通分类
     */
    private String generalSort;
    /**
     * 库存单位
     */
    private String inventoryUnit;
    /**
     * 采购自制件标记
     */
    private String sourceMark;
    /**
     * 责任部门
     */
    private String respDept;
    /**
     * 责任公司
     */
    private String respCompany;
    /**
     * 关键件标记
     */
    private String keyPartMark;
    /**
     * 关键件大类
     */
    private String keyPartSort;
    /**
     * 虚拟件标记
     */
    private String virtualPartMark;
    /**
     * 合批标记
     */
    private String qualifiedMark;

    /**
     * 质检标记
     */
    private String inspectMark;
    /**
     * 批次标记
     */
    private String batchMark;

    /**
     * 采购分类
     */
    private String purchaseSort;
    /**
     * 可采购标记
     */
    private String purchaseMark;
    /**
     * 可集采标记
     */
    private String groupPurMark;
    /**
     * 可自采标记
     */
    private String ownPurMark;

    /**
     * 默认仓库
     */
    private String defRepository;

    /**
     * 外协标记
     */
    private String outSource;
    /**
     * 计划员
     */
    private String planner;
    /**
     * 固定提前期
     */
    private String fixedAdvTime;
    /**
     * 货源标记
     */
    private String resourceMark;

    /**
     * 技术中心完成标记（0-未完善/1-已完善）
     */
    private Integer technologyStatus;
    /**
     * 质量合规部完成标记（0-未完善/1-已完善）
     */
    private Integer qualityStatus;
    /**
     * 采购部完成标记（0-未完善/1-已完善）
     */
    private Integer purchaseStatus;
    /**
     * 集配中心完成标记（0-未完善/1-已完善）
     */
    private Integer assemblyStatus;
    /**
     * 生产部完成标记（0-未完善/1-已完善）
     */
    private Integer produceStatus;
    /**
     * 导出状态（0-待完善/1-可导出/2-已导出）
     */
    private Integer exportStatus;
    /**
     * 负责人
     */
    private String principal;

    /**
     * 令牌
     */
    private String token;


    public Material() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getStructureNo() {
        return structureNo;
    }

    public void setStructureNo(String structureNo) {
        this.structureNo = structureNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInternational() {
        return international;
    }

    public void setInternational(String international) {
        this.international = international;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getGeneralSort() {
        return generalSort;
    }

    public void setGeneralSort(String generalSort) {
        this.generalSort = generalSort;
    }

    public String getInventoryUnit() {
        return inventoryUnit;
    }

    public void setInventoryUnit(String inventoryUnit) {
        this.inventoryUnit = inventoryUnit;
    }

    public String getSourceMark() {
        return sourceMark;
    }

    public void setSourceMark(String sourceMark) {
        this.sourceMark = sourceMark;
    }

    public String getRespDept() {
        return respDept;
    }

    public void setRespDept(String respDept) {
        this.respDept = respDept;
    }

    public String getRespCompany() {
        return respCompany;
    }

    public void setRespCompany(String respCompany) {
        this.respCompany = respCompany;
    }

    public String getKeyPartMark() {
        return keyPartMark;
    }

    public void setKeyPartMark(String keyPartMark) {
        this.keyPartMark = keyPartMark;
    }

    public String getKeyPartSort() {
        return keyPartSort;
    }

    public void setKeyPartSort(String keyPartSort) {
        this.keyPartSort = keyPartSort;
    }

    public String getVirtualPartMark() {
        return virtualPartMark;
    }

    public void setVirtualPartMark(String virtualPartMark) {
        this.virtualPartMark = virtualPartMark;
    }

    public String getQualifiedMark() {
        return qualifiedMark;
    }

    public void setQualifiedMark(String qualifiedMark) {
        this.qualifiedMark = qualifiedMark;
    }

    public String getInspectMark() {
        return inspectMark;
    }

    public void setInspectMark(String inspectMark) {
        this.inspectMark = inspectMark;
    }

    public String getBatchMark() {
        return batchMark;
    }

    public void setBatchMark(String batchMark) {
        this.batchMark = batchMark;
    }

    public String getPurchaseSort() {
        return purchaseSort;
    }

    public void setPurchaseSort(String purchaseSort) {
        this.purchaseSort = purchaseSort;
    }

    public String getPurchaseMark() {
        return purchaseMark;
    }

    public void setPurchaseMark(String purchaseMark) {
        this.purchaseMark = purchaseMark;
    }

    public String getGroupPurMark() {
        return groupPurMark;
    }

    public void setGroupPurMark(String groupPurMark) {
        this.groupPurMark = groupPurMark;
    }

    public String getOwnPurMark() {
        return ownPurMark;
    }

    public void setOwnPurMark(String ownPurMark) {
        this.ownPurMark = ownPurMark;
    }

    public String getDefRepository() {
        return defRepository;
    }

    public void setDefRepository(String defRepository) {
        this.defRepository = defRepository;
    }

    public String getOutSource() {
        return outSource;
    }

    public void setOutSource(String outSource) {
        this.outSource = outSource;
    }

    public String getPlanner() {
        return planner;
    }

    public void setPlanner(String planner) {
        this.planner = planner;
    }

    public String getFixedAdvTime() {
        return fixedAdvTime;
    }

    public void setFixedAdvTime(String fixedAdvTime) {
        this.fixedAdvTime = fixedAdvTime;
    }

    public String getResourceMark() {
        return resourceMark;
    }

    public void setResourceMark(String resourceMark) {
        this.resourceMark = resourceMark;
    }

    public Integer getTechnologyStatus() {
        return technologyStatus;
    }

    public void setTechnologyStatus(Integer technologyStatus) {
        this.technologyStatus = technologyStatus;
    }

    public Integer getQualityStatus() {
        return qualityStatus;
    }

    public void setQualityStatus(Integer qualityStatus) {
        this.qualityStatus = qualityStatus;
    }

    public Integer getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(Integer purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public Integer getAssemblyStatus() {
        return assemblyStatus;
    }

    public void setAssemblyStatus(Integer assemblyStatus) {
        this.assemblyStatus = assemblyStatus;
    }

    public Integer getProduceStatus() {
        return produceStatus;
    }

    public void setProduceStatus(Integer produceStatus) {
        this.produceStatus = produceStatus;
    }

    public Integer getExportStatus() {
        return exportStatus;
    }

    public void setExportStatus(Integer exportStatus) {
        this.exportStatus = exportStatus;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static Material newInstance() {
        Material material = new Material();
        material.setObjectId(Generator.getObjectId());
        material.setStatus(Constant.Status.GENERAL);
        LocalDateTime dateTime = LocalDateTime.now();
        material.setCreateAt(dateTime);
        material.setUpdateAt(dateTime);
        material.setCompanyNo("01");
        material.setVirtualPartMark("N");
        material.setOutSource("N");
        material.setPlanner("QD624");
        material.setTechnologyStatus(Constant.Material.PerfectStatus.IMPERFECT);
        material.setQualityStatus(Constant.Material.PerfectStatus.IMPERFECT);
        material.setPurchaseStatus(Constant.Material.PerfectStatus.IMPERFECT);
        material.setAssemblyStatus(Constant.Material.PerfectStatus.IMPERFECT);
        material.setProduceStatus(Constant.Material.PerfectStatus.IMPERFECT);
        material.setExportStatus(Constant.Material.ExportStatus.IMPERFECT);
        return material;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", companyNo='" + companyNo + '\'' +
                ", structureNo='" + structureNo + '\'' +
                ", code='" + code + '\'' +
                ", originCode='" + originCode + '\'' +
                ", name='" + name + '\'' +
                ", specification='" + specification + '\'' +
                ", model='" + model + '\'' +
                ", description='" + description + '\'' +
                ", international='" + international + '\'' +
                ", drawingNo='" + drawingNo + '\'' +
                ", reference='" + reference + '\'' +
                ", generalSort='" + generalSort + '\'' +
                ", inventoryUnit='" + inventoryUnit + '\'' +
                ", sourceMark='" + sourceMark + '\'' +
                ", respDept='" + respDept + '\'' +
                ", respCompany='" + respCompany + '\'' +
                ", keyPartMark='" + keyPartMark + '\'' +
                ", keyPartSort='" + keyPartSort + '\'' +
                ", virtualPartMark='" + virtualPartMark + '\'' +
                ", qualifiedMark='" + qualifiedMark + '\'' +
                ", inspectMark='" + inspectMark + '\'' +
                ", batchMark='" + batchMark + '\'' +
                ", purchaseSort='" + purchaseSort + '\'' +
                ", purchaseMark='" + purchaseMark + '\'' +
                ", groupPurMark='" + groupPurMark + '\'' +
                ", ownPurMark='" + ownPurMark + '\'' +
                ", defRepository='" + defRepository + '\'' +
                ", outSource='" + outSource + '\'' +
                ", planner='" + planner + '\'' +
                ", fixedAdvTime='" + fixedAdvTime + '\'' +
                ", resourceMark='" + resourceMark + '\'' +
                ", technologyStatus=" + technologyStatus +
                ", qualityStatus=" + qualityStatus +
                ", purchaseStatus=" + purchaseStatus +
                ", assemblyStatus=" + assemblyStatus +
                ", produceStatus=" + produceStatus +
                ", exportStatus=" + exportStatus +
                ", principal='" + principal + '\'' +
                '}' + '\n';
    }
}
