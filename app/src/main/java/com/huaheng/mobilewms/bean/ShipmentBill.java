package com.huaheng.mobilewms.bean;

import java.math.BigDecimal;

public class ShipmentBill {

    /** 物料code */
    private String materialCode;
    /** 出库数量 */
    private BigDecimal qty;
    /** 货主id */
    private String companyId;
    /** 货主code */
        private String companyCode;
    /** 货架*/
    private String location;
    private String shipmentCode;

    public String getShipmentCode() {
        return shipmentCode;
    }

    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ShipmentBill{" +
                "materialCode='" + materialCode + '\'' +
                ", qty=" + qty +
                ", companyId='" + companyId + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
