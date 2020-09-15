package com.huaheng.mobilewms.bean;

import java.math.BigDecimal;

public class ReceiptBill {

    /** 容器编码 */
    String receiptContainerCode;
    /** 物料code */
    String materialCode;
    /** 收货数量 */
    BigDecimal qty;
    /** 库位编码 */
    String locationCode;
    /** 批次号 */
    String batch;
    /** 项目号 */
    String project;

    String receiptDetailId;

    String materialName;
    /** 货主Code */
    String companyCode;
    String receiptCode;

    public String getReceiptContainerCode() {
        return receiptContainerCode;
    }

    public void setReceiptContainerCode(String receiptContainerCode) {
        this.receiptContainerCode = receiptContainerCode;
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

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getReceiptDetailId() {
        return receiptDetailId;
    }

    public void setReceiptDetailId(String receiptDetailId) {
        this.receiptDetailId = receiptDetailId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }

    @Override
    public String toString() {
        return "ReceiptBill{" +
                "receiptContainerCode='" + receiptContainerCode + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", qty=" + qty +
                ", locationCode='" + locationCode + '\'' +
                ", batch='" + batch + '\'' +
                ", project='" + project + '\'' +
                '}';
    }
}
