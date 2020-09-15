package com.huaheng.mobilewms.bean;

import android.graphics.drawable.Drawable;

public class DetailBean {

    private Drawable drawable;
    private String type;
    private String name;
    private String code;
    private boolean complete;
    private int amount;
    private int maxAmount;

    public DetailBean(Drawable drawable, String type, String name, String code) {
        this.drawable = drawable;
        this.type = type;
        this.name = name;
        this.code = code;
        this.complete = false;
        amount = 0;
        maxAmount = Integer.MAX_VALUE;
    }

    public DetailBean(Drawable drawable, String type, String name, String code, boolean complete) {
        this.drawable = drawable;
        this.type = type;
        this.name = name;
        this.code = code;
        this.complete = complete;
        amount = 0;
        maxAmount = Integer.MAX_VALUE;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }
}
