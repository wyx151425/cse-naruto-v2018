package com.cse.naruto.model;

import com.cse.naruto.util.Constant;
import com.cse.naruto.util.Generator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author WangZhenqi
 */
@Entity
@Table(name = "cse_statistic")
public class Statistic extends NarutoEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_statistic")
    @SequenceGenerator(name = "generator_statistic", sequenceName = "sequence_statistic", allocationSize = 1)
    private Integer id;
    /**
     * 标识
     */
    private String token;
    /**
     * 开始时间
     */
    private LocalDateTime startAt;
    /**
     * 技术中心完成时间
     */
    private LocalDateTime technologyCompleteAt;
    /**
     * 质量合规部完成时间
     */
    private LocalDateTime qualityCompleteAt;
    /**
     * 采购部完成时间
     */
    private LocalDateTime purchaseCompleteAt;
    /**
     * 集配中心完成时间
     */
    private LocalDateTime assemblyCompleteAt;
    /**
     * 生产部完成时间
     */
    private LocalDateTime produceCompleteAt;
    /**
     * 导出时间
     */
    private LocalDateTime exportAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getTechnologyCompleteAt() {
        return technologyCompleteAt;
    }

    public void setTechnologyCompleteAt(LocalDateTime technologyCompleteAt) {
        this.technologyCompleteAt = technologyCompleteAt;
    }

    public LocalDateTime getQualityCompleteAt() {
        return qualityCompleteAt;
    }

    public void setQualityCompleteAt(LocalDateTime qualityCompleteAt) {
        this.qualityCompleteAt = qualityCompleteAt;
    }

    public LocalDateTime getPurchaseCompleteAt() {
        return purchaseCompleteAt;
    }

    public void setPurchaseCompleteAt(LocalDateTime purchaseCompleteAt) {
        this.purchaseCompleteAt = purchaseCompleteAt;
    }

    public LocalDateTime getAssemblyCompleteAt() {
        return assemblyCompleteAt;
    }

    public void setAssemblyCompleteAt(LocalDateTime assemblyCompleteAt) {
        this.assemblyCompleteAt = assemblyCompleteAt;
    }

    public LocalDateTime getProduceCompleteAt() {
        return produceCompleteAt;
    }

    public void setProduceCompleteAt(LocalDateTime produceCompleteAt) {
        this.produceCompleteAt = produceCompleteAt;
    }

    public LocalDateTime getExportAt() {
        return exportAt;
    }

    public void setExportAt(LocalDateTime exportAt) {
        this.exportAt = exportAt;
    }

    public static Statistic newInstance() {
        Statistic statistic = new Statistic();
        statistic.setObjectId(Generator.getObjectId());
        statistic.setStatus(Constant.Status.GENERAL);
        LocalDateTime dateTime = LocalDateTime.now().withNano(0);
        statistic.setCreateAt(dateTime);
        statistic.setUpdateAt(dateTime);
        statistic.setToken(Generator.getObjectId());
        statistic.setStartAt(dateTime);
        statistic.setQualityCompleteAt(dateTime);
        return statistic;
    }
}
