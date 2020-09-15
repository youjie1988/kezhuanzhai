package com.huaheng.mobilewms.bean;

import android.graphics.drawable.Drawable;

public class ChooseBean {

    private Drawable drawable;
    private String name;

    public ChooseBean(Drawable drawable, String name) {
        this.drawable = drawable;
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ChooseBean{" +
                "drawable=" + drawable +
                ", name='" + name + '\'' +
                '}';
    }
}
