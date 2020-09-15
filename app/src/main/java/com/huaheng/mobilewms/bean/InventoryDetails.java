package com.huaheng.mobilewms.bean;

import java.math.BigDecimal;

public class InventoryDetails {

    private String date;
    private BigDecimal qty;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "InventoryDetails{" +
                "date='" + date + '\'' +
                ", qty=" + qty +
                '}';
    }
}
