package com.huaheng.mobilewms.bean;

import android.graphics.drawable.Drawable;
import android.view.View;

public class MenuBean {

    private Drawable menu;
    private String name;
    private View.OnClickListener listener;

    public MenuBean(Drawable menu, String name) {
        this.menu = menu;
        this.name = name;
    }

    public Drawable getMenu() {
        return menu;
    }

    public void setMenu(Drawable menu) {
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public String toString() {
        return "MenuBean{" +
                "menu=" + menu +
                ", name='" + name + '\'' +
                '}';
    }
}
