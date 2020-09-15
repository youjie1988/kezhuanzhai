package com.huaheng.mobilewms.bean;

public class GoodsInfo {

    private float qtyCompleted;
    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQtyCompleted(float qty) {
        this.qtyCompleted = qty;
    }

    public float getQtyCompleted() {
        return qtyCompleted;
    }

    @Override
    public String toString() {
        return "GoodsInfo{" +
                "qtyCompleted=" + qtyCompleted +
                ", id='" + id + '\'' +
                '}';
    }
}
