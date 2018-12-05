package com.will.talentreview.entity;

import android.text.TextUtils;

import com.will.talentreview.application.MyApplication;
import com.will.talentreview.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenwei
 * @time 2018-11-26
 */

public class RequestParam {

    private HashMap<String, Object> params;

    public RequestParam() {
        params = new HashMap<>();
        LoginInfo loginInfo = MyApplication.getInstance().getLoginInfo();
        if (loginInfo != null) {
            params.put("token", loginInfo.getToken());
            params.put("userId", StringUtils.excludeNull(loginInfo.getId(),"0"));
        }else {
            params.put("userId",0);
        }
    }

    public void addParam(String key, Object value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        params.put(key, value);
    }

    public Map getParams() {
        return params;
    }

    public void clearExceptDefault(){
        params.clear();
        LoginInfo loginInfo = MyApplication.getInstance().getLoginInfo();
        if (loginInfo != null) {
            params.put("token", loginInfo.getToken());
            params.put("userId", StringUtils.excludeNull(loginInfo.getId(),"0"));
        }else {
            params.put("userId",0);
        }
    }
}
