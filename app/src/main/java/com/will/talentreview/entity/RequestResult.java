package com.will.talentreview.entity;

import java.util.List;

/**
 * @author chenwei
 * @time 2018-11-22
 */

public class RequestResult<T> {
    public static final String MSG = "msg";
    public static final String CODE = "code";
    public static final String DATA = "data";

    private boolean success;
    private String msg;
    private int code;
    private List<T> datas;
    private T data;
    private List<ListBean<T>> list;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ListBean<T>> getList() {
        return list;
    }

    public void setList(List<ListBean<T>> list) {
        this.list = list;
    }
}
