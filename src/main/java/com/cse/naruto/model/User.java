package com.cse.naruto.model;

import javax.persistence.*;
import java.util.Map;

/**
 * 用户类
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/10/12
 */
@Entity
@Table(name = "cse_user")
public class User extends NarutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_user")
    @SequenceGenerator(name = "generator_user", sequenceName = "sequence_user", allocationSize = 1)
    private Integer id;
    private String username;
    private String name;
    private String password;
    private String roles;
    @Transient
    private Map<String, Boolean> permissions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }
}
