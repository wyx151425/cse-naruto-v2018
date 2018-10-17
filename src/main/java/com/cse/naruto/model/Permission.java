package com.cse.naruto.model;

import javax.persistence.*;

/**
 * 用户权限组
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/10/12
 */
@Entity
@Table(name = "cse_permission")
public class Permission extends NarutoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_permission")
    @SequenceGenerator(name = "generator_permission", sequenceName = "sequence_permission", allocationSize = 1)
    private Integer id;
    private String role;
    private String name;

    public Permission() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
