package com.huaheng.mobilewms.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class ReceiptInfo {

    @Id
    private long _id;
    private float totalQty;   //总共多少个
    private float openQty;     //已收多少个
    private float qty;
    private String unit;    //单位
    private String batch;  //批次号
    private String project;  //项目号
    private String materialName;
    private String materialCode;
    private String id;
    private String receiptCode;
    private String receiptId;
    private String quality;
    private String spec;
    @Generated(hash = 1880018685)
    public ReceiptInfo(long _id, float totalQty, float openQty, float qty,
            String unit, String batch, String project, String materialName,
            String materialCode, String id, String receiptCode, String receiptId,
            String quality, String spec) {
        this._id = _id;
        this.totalQty = totalQty;
        this.openQty = openQty;
        this.qty = qty;
        this.unit = unit;
        this.batch = batch;
        this.project = project;
        this.materialName = materialName;
        this.materialCode = materialCode;
        this.id = id;
        this.receiptCode = receiptCode;
        this.receiptId = receiptId;
        this.quality = quality;
        this.spec = spec;
    }
    @Generated(hash = 1137185615)
    public ReceiptInfo() {
    }
    public long get_id() {
        return this._id;
    }
    public void set_id(long _id) {
        this._id = _id;
    }
    public float getTotalQty() {
        return this.totalQty;
    }
    public void setTotalQty(float totalQty) {
        this.totalQty = totalQty;
    }
    public float getOpenQty() {
        return this.openQty;
    }
    public void setOpenQty(float openQty) {
        this.openQty = openQty;
    }
    public float getQty() {
        return this.qty;
    }
    public void setQty(float qty) {
        this.qty = qty;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getBatch() {
        return this.batch;
    }
    public void setBatch(String batch) {
        this.batch = batch;
    }
    public String getProject() {
        return this.project;
    }
    public void setProject(String project) {
        this.project = project;
    }
    public String getMaterialName() {
        return this.materialName;
    }
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    public String getMaterialCode() {
        return this.materialCode;
    }
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getReceiptCode() {
        return this.receiptCode;
    }
    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }
    public String getReceiptId() {
        return this.receiptId;
    }
    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }
    public String getQuality() {
        return this.quality;
    }
    public void setQuality(String quality) {
        this.quality = quality;
    }
    public String getSpec() {
        return this.spec;
    }
    public void setSpec(String spec) {
        this.spec = spec;
    }

    @Override
    public String toString() {
        return "ReceiptInfo{" +
                "_id=" + _id +
                ", totalQty=" + totalQty +
                ", openQty=" + openQty +
                ", qty=" + qty +
                ", unit='" + unit + '\'' +
                ", batch='" + batch + '\'' +
                ", project='" + project + '\'' +
                ", materialName='" + materialName + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", id='" + id + '\'' +
                ", receiptCode='" + receiptCode + '\'' +
                ", receiptId='" + receiptId + '\'' +
                ", quality='" + quality + '\'' +
                ", spec='" + spec + '\'' +
                '}';
    }
}
