package com.huaheng.mobilewms.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UserBean {

    private String code;
    private String name;
    private int id;

    public UserBean(String code, String name, int id) {
        this.code = code;
        this.name = name;
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

}
