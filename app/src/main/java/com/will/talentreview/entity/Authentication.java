package com.will.talentreview.entity;

/**
 * @author chenwei
 * @time 2018-12-03
 * 认证信息
 */

public class Authentication {
    private String id;
    private String cardNo;
    private String userName;
    private String validity;
    private String fontPicUrl;
    private String behindPicUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getFontPicUrl() {
        return fontPicUrl;
    }

    public void setFontPicUrl(String fontPicUrl) {
        this.fontPicUrl = fontPicUrl;
    }

    public String getBehindPicUrl() {
        return behindPicUrl;
    }

    public void setBehindPicUrl(String behindPicUrl) {
        this.behindPicUrl = behindPicUrl;
    }
}
