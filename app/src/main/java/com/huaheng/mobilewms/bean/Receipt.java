package com.huaheng.mobilewms.bean;

import java.util.List;

public class Receipt {

    private ReceiptHeader receiptHeader;

    private List<ReceiptDetail> receiptDetails;

    public ReceiptHeader getReceiptHeader() {
        return receiptHeader;
    }

    public void setReceiptHeader(ReceiptHeader receiptHeader) {
        this.receiptHeader = receiptHeader;
    }

    public List <ReceiptDetail> getReceiptDetails() {
        return receiptDetails;
    }

    public void setReceiptDetails(List <ReceiptDetail> receiptDetails) {
        this.receiptDetails = receiptDetails;
    }
}