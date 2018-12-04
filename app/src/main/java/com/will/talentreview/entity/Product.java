package com.will.talentreview.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenwei
 * @time 2018-11-20
 * 产品实体类
 */

public class Product implements Serializable{
    private String id;
    //名称
    private String productName;
    //序号
    private String productNo;
    //年限
    private String annualInterestRate;
    //收益
    private String purchaseMoney;
    private String productClass;
    //风评和解析的地址
    private String productType;
    private String behindType;
    private String productLabel;
    private String productRule;
    private String ageLimit;
    private String moenUse;
    private String fundingAgencies;
    private String guaranteeAgencies;
    private String issuer;
    private String isCollection;
    private String isBought;
    private String url;
    private List<ProductEarn> earningsEntityList;
    private List<BrightPoint> brohtLightEntityList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(String annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public String getPurchaseMoney() {
        return purchaseMoney;
    }

    public void setPurchaseMoney(String purchaseMoney) {
        this.purchaseMoney = purchaseMoney;
    }

    public String getProductClass() {
        return productClass;
    }

    public void setProductClass(String productClass) {
        this.productClass = productClass;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBehindType() {
        return behindType;
    }

    public void setBehindType(String behindType) {
        this.behindType = behindType;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public void setProductLabel(String productLabel) {
        this.productLabel = productLabel;
    }

    public String getProductRule() {
        return productRule;
    }

    public void setProductRule(String productRule) {
        this.productRule = productRule;
    }

    public String getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(String ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getMoenUse() {
        return moenUse;
    }

    public void setMoenUse(String moenUse) {
        this.moenUse = moenUse;
    }

    public String getFundingAgencies() {
        return fundingAgencies;
    }

    public void setFundingAgencies(String fundingAgencies) {
        this.fundingAgencies = fundingAgencies;
    }

    public String getGuaranteeAgencies() {
        return guaranteeAgencies;
    }

    public void setGuaranteeAgencies(String guaranteeAgencies) {
        this.guaranteeAgencies = guaranteeAgencies;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ProductEarn> getEarningsEntityList() {
        return earningsEntityList;
    }

    public void setEarningsEntityList(List<ProductEarn> earningsEntityList) {
        this.earningsEntityList = earningsEntityList;
    }

    public List<BrightPoint> getBrohtLightEntityList() {
        return brohtLightEntityList;
    }

    public void setBrohtLightEntityList(List<BrightPoint> brohtLightEntityList) {
        this.brohtLightEntityList = brohtLightEntityList;
    }

    public String getIsBought() {
        return isBought;
    }

    public void setIsBought(String isBought) {
        this.isBought = isBought;
    }
}
