package com.will.talentreview.entity;

/**
 * @author chenwei
 * @time 2018-12-05
 * 产品筛选
 */

public class ProductFilter {
    private String startAgeLimit;
    private String endAgeLimit;
    private String startEarnings;
    private String endEarnings;
    private String startMoney;
    private String endMoney;
    private String productClassId;
    private String productTypeId;
    private String underlyingTypeId;

    public String getStartAgeLimit() {
        return startAgeLimit;
    }

    public void setStartAgeLimit(String startAgeLimit) {
        this.startAgeLimit = startAgeLimit;
    }

    public String getEndAgeLimit() {
        return endAgeLimit;
    }

    public void setEndAgeLimit(String endAgeLimit) {
        this.endAgeLimit = endAgeLimit;
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

    public String getStartMoney() {
        return startMoney;
    }

    public void setStartMoney(String startMoney) {
        this.startMoney = startMoney;
    }

    public String getEndMoney() {
        return endMoney;
    }

    public void setEndMoney(String endMoney) {
        this.endMoney = endMoney;
    }

    public String getProductClassId() {
        return productClassId;
    }

    public void setProductClassId(String productClassId) {
        this.productClassId = productClassId;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getUnderlyingTypeId() {
        return underlyingTypeId;
    }

    public void setUnderlyingTypeId(String underlyingTypeId) {
        this.underlyingTypeId = underlyingTypeId;
    }
}
