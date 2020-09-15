package com.huaheng.mobilewms.bean;

/**
 * Created by youjie on 2018/9/29
 */
public class MaterialInfo {

    private String materialCode;

    private String materialName;

    private String qty;

    private String type;

    private String batch;

    private String project;

    private String receiptDetailId;
    private Integer billDetailId;

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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
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

    public Integer getBillDetailId() {
        return billDetailId;
    }

    public void setBillDetailId(Integer billDetailId) {
        this.billDetailId = billDetailId;
    }

    @Override
    public String toString() {
        return "MaterialInfo{" +
                "materialCode='" + materialCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", qty='" + qty + '\'' +
                ", type='" + type + '\'' +
                ", batch='" + batch + '\'' +
                ", project='" + project + '\'' +
                '}';
    }
}
