package com.huaheng.mobilewms.bean;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

public class ShipmentMaterialDetail implements Comparable<ShipmentMaterialDetail>{

    private String materialName;
    private BigDecimal qty;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "ShipmentDetail{" +
                "materialName='" + materialName + '\'' +
                ", qty=" + qty +
                '}';
    }


    @Override
    public int compareTo(@NonNull ShipmentMaterialDetail o) {
       BigDecimal compare  = this.getQty().subtract(o.getQty());
       int result = compare.intValue();
       return -result;
    }
}
