package com.will.talentreview.entity;

import java.io.Serializable;

/**
 * @author chenwei
 * @time 2018-11-30
 * 产品收益实体类
 */

public class ProductEarn implements Serializable{
    private String id;
    private String productId;
    private String startEarnings;
    private String endEarnings;
    private String earnings;
    private int sort;
    private String createTime;
    private String createUser;
    private int isDelete;
    private String updateTime;
    private String updateUser;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStartEarnings() {
        return startEarnings;
    }

    public void setStartEarnings(String startEarnings) {
        this.startEarnings = startEarnings;
    }

    public String getEndEarnings() {
        return endEarnings;
    }

    public void setEndEarnings(String endEarnings) {
        this.endEarnings = endEarnings;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
