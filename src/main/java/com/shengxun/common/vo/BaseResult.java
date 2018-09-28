package com.shengxun.common.vo;

import java.io.Serializable;

public class BaseResult<T> implements Serializable{

    private static final long serialVersionUID = 872931113963645452L;

    private String code;
    private String message;
    private String total;
    private T data;

    public BaseResult(){}

    public BaseResult(String code, String message, String total, T data){
        this.code = code;
        this.message = message;
        this.total = total;
        this.data = data;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}