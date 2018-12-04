package com.will.talentreview.entity;

/**
 * @author chenwei
 * @time 2018-11-28
 */

public class LoginResult {
    private String token;
    private String expire;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }
}
