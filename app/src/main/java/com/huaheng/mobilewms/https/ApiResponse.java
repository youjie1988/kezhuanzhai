package com.huaheng.mobilewms.https;

public class ApiResponse<T> {

    private String code;    // 返回码
    private String msg;      // 返回信息
    private T data;
    private T session;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getSession() {
        return session;
    }

    public void setSession(T session) {
        this.session = session;
    }
}
