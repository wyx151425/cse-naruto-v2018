package com.cse.naruto.model;

import com.cse.naruto.util.Constant;
import com.cse.naruto.util.StatusCode;

import java.util.List;

/**
 * 用户类
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/10/12
 */
public class PageContext<T> {
    private int index;
    private int size;
    private long dataTotal;
    private int pageTotal;
    private List<T> data;

    public PageContext() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getDataTotal() {
        return dataTotal;
    }

    public void setDataTotal(long dataTotal) {
        this.dataTotal = dataTotal;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
