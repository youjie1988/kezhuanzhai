package com.huaheng.mobilewms.bean;

import java.math.BigDecimal;

public class ShipmentDetail {

    private Integer id;
    private Integer shipmentId;
    private String warehouseCode;
    private String companyCode;
    private String shipmentCode;
    private String referCode;
    private Integer referId;
    private String referLineNum;
    private String materialCode;
    private String materialName;
    private String materialUnit;
    private BigDecimal shipQty;
    private BigDecimal requestQty;
    private int qty;
    private String batch;
    private Integer waveId;
    private String created;
    private String createdBy;
    private String lastUpdated;
    private String lastUpdatedBy;
    private Integer version;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getShipmentCode() {
        return shipmentCode;
    }

    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public Integer getReferId() {
        return referId;
    }

    public void setReferId(Integer referId) {
        this.referId = referId;
    }

    public String getReferLineNum() {
        return referLineNum;
    }

    public void setReferLineNum(String referLineNum) {
        this.referLineNum = referLineNum;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public BigDecimal getShipQty() {
        return shipQty;
    }

    public void setShipQty(BigDecimal shipQty) {
        this.shipQty = shipQty;
    }

    public BigDecimal getRequestQty() {
        return requestQty;
    }

    public void setRequestQty(BigDecimal requestQty) {
        this.requestQty = requestQty;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Integer getWaveId() {
        return waveId;
    }

    public void setWaveId(Integer waveId) {
        this.waveId = waveId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "ShipmentDetail{" +
                "id=" + id +
                ", shipmentId=" + shipmentId +
                ", warehouseCode='" + warehouseCode + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", shipmentCode='" + shipmentCode + '\'' +
                ", referCode='" + referCode + '\'' +
                ", referId=" + referId +
                ", referLineNum='" + referLineNum + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", materialUnit='" + materialUnit + '\'' +
                ", shipQty=" + shipQty +
                ", requestQty=" + requestQty +
                ", batch='" + batch + '\'' +
                ", waveId=" + waveId +
                ", created='" + created + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", version=" + version +
                '}';
    }
}