package com.itheima.domain;

import java.util.List;

public class PageBean<T> {
//    当前页
private int currentPage;
//    每页条数
private int currentCount;
//    总记录数
private int totalCount;
//    总页数
private int totalPage;
//    data存放商品信息
private List<T> list;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
