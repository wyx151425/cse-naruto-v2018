package com.cse.naruto.model;

public class DeptPerfect {
    private String name;
    private Integer imperfect;
    private Integer perfected;

    public DeptPerfect(String name, Integer imperfect, Integer perfected) {
        this.name = name;
        this.imperfect = imperfect;
        this.perfected = perfected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImperfect() {
        return imperfect;
    }

    public void setImperfect(Integer imperfect) {
        this.imperfect = imperfect;
    }

    public Integer getPerfected() {
        return perfected;
    }

    public void setPerfected(Integer perfected) {
        this.perfected = perfected;
    }
}
