package com.huaheng.mobilewms.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by youjie on 2020/6/4
 */
public class InventoryDetailBean {

    private Drawable drawable;
    private String containerCode;
    private String code;
    private String name;
    private String amount;

    public InventoryDetailBean(Drawable drawable, String containerCode, String code, String name, String amount) {
        this.drawable = drawable;
        this.containerCode = containerCode;
        this.code = code;
        this.name = name;
        this.amount = amount;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
