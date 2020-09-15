package com.huaheng.mobilewms.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.huaheng.mobilewms.R;
import java.util.ArrayList;

public class MenuConstant {

    public static ArrayList<String> getMenuList(Context context) {
        ArrayList<String> list = new ArrayList<>();
        list.add(context.getString(R.string.receipt));
        list.add(context.getString(R.string.shipment));
        list.add(context.getString(R.string.task));
        list.add(context.getString(R.string.menu_inventory));
        list.add(context.getString(R.string.materialManager));
        list.add(context.getString(R.string.printer));
        list.add(context.getString(R.string.setting));
        return list;
    }

    public static ArrayList<Drawable> getMenuDrawable(Context context) {
        ArrayList<Drawable> list = new ArrayList<>();
        list.add(context.getResources().getDrawable(R.mipmap.menu_receipt));
        list.add(context.getResources().getDrawable(R.mipmap.menu_shipment));
        list.add(context.getResources().getDrawable(R.mipmap.menu_icon_inventory));
        list.add(context.getResources().getDrawable(R.mipmap.menu_icon_query));
        list.add(context.getResources().getDrawable(R.mipmap.menu_material));
        list.add(context.getResources().getDrawable(R.mipmap.menu_icon_printer));
        list.add(context.getResources().getDrawable(R.mipmap.menu_icon_setting));
        return list;
    }
}
