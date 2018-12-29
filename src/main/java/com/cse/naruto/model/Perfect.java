package com.cse.naruto.model;

import java.util.List;

public class Perfect {
    private Integer count;
    private List<DeptPerfect> deptPerfects;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<DeptPerfect> getDeptPerfects() {
        return deptPerfects;
    }

    public void setDeptPerfects(List<DeptPerfect> deptPerfects) {
        this.deptPerfects = deptPerfects;
    }
}
