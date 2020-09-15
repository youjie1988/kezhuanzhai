package com.huaheng.mobilewms.util;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.WMSApplication;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.ReceiptBill;
import com.huaheng.mobilewms.view.EditLayout;
import com.huaheng.mobilewms.view.LineLayout;
import com.huaheng.mobilewms.view.MaterialEditLayout;
import com.huaheng.mobilewms.view.SpinnerLayout;
import com.huaheng.mobilewms.view.TypeSpinnerLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WMSUtils {

    private static boolean isShow = true;
    private String regex = "^[a-zA-Z]";
    public static String DM = "DM";
    public static long startTime = 0;
    public static long endTime = 0;
    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(String message)
    {
        if (isShow)
            Toast.makeText(WMSApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(String message)
    {
        if (isShow)
            Toast.makeText(WMSApplication.getContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void requestFocus(final View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.requestFocus();
            }
        }, 300);
    }

    public static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public static String getVersionName() {
        Context context = WMSApplication.getContext();
        String localVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            localVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersionName;
    }

    public static int getVersionCode() {
        Context context = WMSApplication.getContext();
        int localVersion = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public static String getPackageName() {
        Context context = WMSApplication.getContext();
        String pkgName = null;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            pkgName = packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pkgName;
    }

    public static void saveData(String key, String data) {
        Context context = WMSApplication.getContext();
        SharedPreferences sp = context.getSharedPreferences("wms", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, data);
        editor.commit();
    }

    public static String getData(String key) {
        Context context = WMSApplication.getContext();
        SharedPreferences sp = context.getSharedPreferences("wms", Activity.MODE_PRIVATE);
        String data = sp.getString(key, null);
        return data;
    }

    public static String getData(String key, String value) {
        Context context = WMSApplication.getContext();
        SharedPreferences sp = context.getSharedPreferences("wms", Activity.MODE_PRIVATE);
        String data = sp.getString(key, value);
        return data;
    }

    public static void resetEdit(EditText editText) {
        editText.setText("");
        WMSUtils.requestFocus(editText);
    }

    public static boolean isNotEmpty(String str) {
        if(str != null && str.length() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        if(str == null || str.length() < 1) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmptyList(List<?> list) {
        if(list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean containLetter(String text) {
        boolean result = false;
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(text);
        if(m.matches()){
            result = true;
        }
        WMSLog.d("containLetter  text:" + result);
        return  result;
    }

    public static LineLayout newContent(Context context, String title, String content) {
        LineLayout lineLayout = new LineLayout(context);
        lineLayout.setLineName(title);
        lineLayout.setLineContent(content);
        return lineLayout;
    }

    public static EditLayout newEdit(Context context, String title) {
        EditLayout editLayout = new EditLayout(context);
        editLayout.setEditName(title);
        return editLayout;
    }

    public static MaterialEditLayout newMaterialEdit(Context context, String title) {
        MaterialEditLayout editLayout = new MaterialEditLayout(context);
        editLayout.setEditName(title);
        return editLayout;
    }

    public static SpinnerLayout newSpinner(Context context, String title) {
        SpinnerLayout editLayout = new SpinnerLayout(context);
        editLayout.setLineName(title);
        return editLayout;
    }

    public static TypeSpinnerLayout newTypeSpinner(Context context, String title) {
        TypeSpinnerLayout editLayout = new TypeSpinnerLayout(context);
        editLayout.setLineName(title);
        return editLayout;
    }


    public static View newDevider(Context context) {
        View view = new View(context);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2);
        view.setMinimumHeight(2);
        view.setMinimumWidth(-1);
        view.setBackgroundResource(R.color.black);
        view.setLayoutParams(params);
        return view;
    }

    // 计算文件的 MD5 值
    public static String md5(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream in = null;
        String result = "";
        byte buffer[] = new byte[8192];
        int len;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            byte[] bytes = md5.digest();

            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null!=in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static void requestAppPermission(Activity activity) {
        requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        requestPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void requestPermission(Activity activity, String permission) {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {

            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限

            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    /*
     ** 判断JsonData里是否有该key，预防没有key直接造成奔溃
     */
    public static String getJsonData(JSONObject object, String key) {
        String result = null;
        try {
            result = object.has(key) ? object.getString(key) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    public static void onKeyEvent(final int keyCode) {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void acquireWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WMS");
        wakeLock.acquire();
    }

    public static void releaseWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WMS");
        wakeLock.release();
    }

    public static String replaceLocation(String location) {
        if(!isNotEmpty(location)) {
            return location;
        }
        if(location.substring(0,1).equals("L") && location.length() == 7) {
            String result = location.substring(0,3) + "_" + location.substring(3,5) + "_" + location.substring(5,7);
            WMSLog.d("result:" + result);
            return result;
        }
//        if(location.substring(0,1).equals("L") && location.length() == 9) {
//            String result = location.substring(0,3) + "_" + location.substring(3,5) + "_" + location.substring(5,7) + "_" + location.substring(7,9);
//            WMSLog.d("result:" + result);
//            return result;
//        }
        if(location.length() > 2 && location.substring(0,2).equals("DM") && location.length() == 10) {
            String result = location.substring(0,4) + "_" + location.substring(4,6) + "_" + location.substring(6,8) + "_" + location.substring(8,10);
            return result;
        }
        return location;
    }

    public static boolean isLocation(String number) {
        char first = number.charAt(0);
        if(number.length() == 4 || number.length() == 7) {
            return true;
        }

        if(((first>='a' && first<='z') || (first>='A' && first<='Z'))) {
            return true;
        } else {
            return false;
        }
    }

    public static int getDpi(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }

    public static void setDpi(Context mContext, int dpi) {
        Resources mResources = mContext.getResources();
        Configuration config = mResources.getConfiguration();
        config.densityDpi = dpi;
        DisplayMetrics metrics = mResources.getDisplayMetrics();
        mResources.updateConfiguration(config, metrics);
    }

    public static DatesTime freshTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        DatesTime dateTime = new DatesTime(year, month, day, hour, minute, second);
        WMSLog.d("freshTime  month:" + month + "  day:" + day + "   hour:" + hour);
        return dateTime;
    }

    public static class DatesTime {
        int year, month, day, hour, minute, second;

        public DatesTime(int year, int month, int day, int hour, int minute, int second) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }
    }


    public static void setOnDatePick(final Context mContext, final EditText editText) {
        final WMSUtils.DatesTime mDateTime = WMSUtils.freshTime();
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                    String str = editText.getText().toString();
                                    editText.setText(str + " " + hour + ":" + minute + ":" + "00");
                                }
                            }, mDateTime.getHour(), mDateTime.getMinute(), true).show();
                            editText.setText(year + "-" + (month + 1) + "-" + day);
                        }
                    }, mDateTime.getYear(), mDateTime.getMonth() - 1, mDateTime.getDay()).show();
                }
                return false;
            }
        });
        editText.setInputType(InputType.TYPE_NULL);
    }

    /**
     *  校验数量比如大于0
     * @return
     */
    public static boolean checkNumber(ArrayList<ReceiptBill> receiptBills) {
        for(ReceiptBill receiptBill : receiptBills) {
            BigDecimal qty = receiptBill.getQty();
            if(qty.compareTo(new BigDecimal(0)) <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return
     */
    public static ArrayList<ReceiptBill> removeUnUseList(ArrayList<ReceiptBill> receiptBills) {
        ArrayList<ReceiptBill> removeReceiptBills = new ArrayList <>();
        for(ReceiptBill receiptBill : receiptBills) {
            BigDecimal qty = receiptBill.getQty();
            if(qty.compareTo(new BigDecimal(0)) <= 0) {
                removeReceiptBills.add(receiptBill);
            }
        }
        receiptBills.removeAll(removeReceiptBills);
        return receiptBills;
    }

    /**
     * 解析出入库单中的物料编码
     * "﻿10310100129/绘图纸,A0/880MM*50M/80G/sfs/10/PI2020061800003","
     * @param str
     * @return
     */
    public static String getMaterialCode(String str){
        try{
            str = str.split(Constant.SEPARATOR_STRING)[0];
            str = WMSUtils.filterUTF8StringBomHeader(str);
        }catch (Exception e){

        }finally {
            return str;
        }
    }

    /**
     * 过滤UTF8字符串中的Bom头 '\uFEFF'
     * @param str
     * @return
     */
    public static String filterUTF8StringBomHeader(String str){
        try{
            char firstChar = str.charAt(0);
            if(firstChar == '\uFEFF'){
                str = str.substring(1);
                Log.i("去掉字UTF8符串Bom头 '\\uFEFF'",str);
            }
        }catch (Exception e){

        }finally {
            return str;
        }
    }
}
