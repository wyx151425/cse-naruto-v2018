package com.cse.naruto.model;

import javax.persistence.*;
import java.util.Map;

/**
 * 用户类
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/11/29
 */
@Entity
@Table(name = "cse_user")
public class User extends NarutoEntity {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_user")
    @SequenceGenerator(name = "generator_user", sequenceName = "sequence_user", allocationSize = 1)
    private Integer id;
    private String code;
    private String name;
    private String password;
    private String role;
    @Transient
    private Map<String, Boolean> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Map<String, Boolean> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, Boolean> roles) {
        this.roles = roles;
    }
}
