package com.huaheng.mobilewms.bean;

public class ShipmentMaterial {

    private String materialCode;
    private String containerCode;
    private String locaitonCode;
    private String companyCode;
    private int shipmentDetailId;
    private int qty;

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getLocaitonCode() {
        return locaitonCode;
    }

    public void setLocaitonCode(String locaitonCode) {
        this.locaitonCode = locaitonCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public int getShipmentDetailId() {
        return shipmentDetailId;
    }

    public void setShipmentDetailId(int shipmentDetailId) {
        this.shipmentDetailId = shipmentDetailId;
    }
}
