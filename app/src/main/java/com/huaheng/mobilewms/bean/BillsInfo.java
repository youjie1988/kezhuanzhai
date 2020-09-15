package com.huaheng.mobilewms.bean;

public class BillsInfo {

    private String receiptContainerCode;
    private String loation;
    private String receiptDetailId;
    private String qty;

    public BillsInfo(String receiptContainerCode, String loation, String receiptDetailId, String qty) {
        this.receiptContainerCode = receiptContainerCode;
        this.loation = loation;
        this.receiptDetailId = receiptDetailId;
        this.qty = qty;
    }

    public String getReceiptContainerCode() {
        return receiptContainerCode;
    }

    public void setReceiptContainerCode(String receiptContainerCode) {
        this.receiptContainerCode = receiptContainerCode;
    }

    public String getReceiptDetailId() {
        return receiptDetailId;
    }

    public void setReceiptDetailId(String receiptDetailId) {
        this.receiptDetailId = receiptDetailId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getLoation() {
        return loation;
    }

    public void setLoation(String loation) {
        this.loation = loation;
    }

    @Override
    public String toString() {
        return "BillsInfo{" +
                "receiptContainerCode='" + receiptContainerCode + '\'' +
                ", receiptDetailId='" + receiptDetailId + '\'' +
                ", qty='" + qty + '\'' +
                '}';
    }
}
