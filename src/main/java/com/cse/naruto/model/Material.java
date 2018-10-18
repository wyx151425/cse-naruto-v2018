package com.cse.naruto.model;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_material")
    @SequenceGenerator(name = "generator_material", sequenceName = "sequence_material", allocationSize = 1)
    private Integer id;
    /**
     * 公司号
     */
    private String companyNo;
    /**
     * 物料编码
     */
    private String code;
    /**
     * 物料名称
     */
    private String name;
    /**
     * 物料简称
     */
    private String shortName;
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
     * 图号
     */
    private String drawingNo;
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
     * 技术中心完成标记
     */
    private Boolean technologyDeptMark;
    /**
     * 质量环保部完成标记
     */
    private Boolean qualifiedDeptMark;
    /**
     * 采购部完成标记
     */
    private Boolean purchaseDeptMark;
    /**
     * 集配中心完成标记
     */
    private Boolean assemblyDeptMark;
    /**
     * 生产运行部完成标记
     */
    private Boolean operateDeptMark;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
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

    public Boolean getTechnologyDeptMark() {
        return technologyDeptMark;
    }

    public void setTechnologyDeptMark(Boolean technologyDeptMark) {
        this.technologyDeptMark = technologyDeptMark;
    }

    public Boolean getQualifiedDeptMark() {
        return qualifiedDeptMark;
    }

    public void setQualifiedDeptMark(Boolean qualifiedDeptMark) {
        this.qualifiedDeptMark = qualifiedDeptMark;
    }

    public Boolean getPurchaseDeptMark() {
        return purchaseDeptMark;
    }

    public void setPurchaseDeptMark(Boolean purchaseDeptMark) {
        this.purchaseDeptMark = purchaseDeptMark;
    }

    public Boolean getAssemblyDeptMark() {
        return assemblyDeptMark;
    }

    public void setAssemblyDeptMark(Boolean assemblyDeptMark) {
        this.assemblyDeptMark = assemblyDeptMark;
    }

    public Boolean getOperateDeptMark() {
        return operateDeptMark;
    }

    public void setOperateDeptMark(Boolean operateDeptMark) {
        this.operateDeptMark = operateDeptMark;
    }
}
