package com.cse.naruto.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/09/19
 */
@MappedSuperclass
public class NarutoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String objectId;
    private Integer status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public NarutoEntity() {
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
