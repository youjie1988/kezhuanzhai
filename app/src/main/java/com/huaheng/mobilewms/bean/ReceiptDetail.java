package com.huaheng.mobilewms.bean;

public class ReceiptDetail {

    private Integer id;
    private Integer receiptId;
    private String receiptCode;
    private String warehouseCode;
    private String companyCode;
    private String materialCode;
    private String materialName;
    private String materialSpec;
    private String materialUnit;
    private String supplierCode;
    private String batch;
    private String lot;
    private String qcCheck;
    private String projectNo;
    private String manufactureDate;
    private String expirationDate;
    private String agingDate;
    private String attributeTemplateCode;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private float totalQty;
    private float openQty;
    private int qty = 0;
    private String referCode;
    private Integer referId;
    private String referLineNum;
    private String locatingRule;
    private String inventorySts;
    private float itemListPrice;
    private float itemNetPrice;
    private Integer isVirtualBom;
    private String created;
    private String createdBy;
    private String lastUpdated;
    private String lastUpdatedBy;
    private Integer version;
    private String userDef1;
    private String userDef2;
    private String userDef3;
    private String userDef4;
    private String userDef5;
    private String userDef6;
    private String userDef7;
    private String userDef8;
    private String processStamp;
    private Boolean deleted;
    private String statusFlowCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Integer getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Integer receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
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

    public String getMaterialSpec() {
        return materialSpec;
    }

    public void setMaterialSpec(String materialSpec) {
        this.materialSpec = materialSpec;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getQcCheck() {
        return qcCheck;
    }

    public void setQcCheck(String qcCheck) {
        this.qcCheck = qcCheck;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getAgingDate() {
        return agingDate;
    }

    public void setAgingDate(String agingDate) {
        this.agingDate = agingDate;
    }

    public String getAttributeTemplateCode() {
        return attributeTemplateCode;
    }

    public void setAttributeTemplateCode(String attributeTemplateCode) {
        this.attributeTemplateCode = attributeTemplateCode;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public float getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(float totalQty) {
        this.totalQty = totalQty;
    }

    public float getOpenQty() {
        return openQty;
    }

    public void setOpenQty(float openQty) {
        this.openQty = openQty;
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

    public String getLocatingRule() {
        return locatingRule;
    }

    public void setLocatingRule(String locatingRule) {
        this.locatingRule = locatingRule;
    }

    public String getInventorySts() {
        return inventorySts;
    }

    public void setInventorySts(String inventorySts) {
        this.inventorySts = inventorySts;
    }

    public float getItemListPrice() {
        return itemListPrice;
    }

    public void setItemListPrice(float itemListPrice) {
        this.itemListPrice = itemListPrice;
    }

    public float getItemNetPrice() {
        return itemNetPrice;
    }

    public void setItemNetPrice(float itemNetPrice) {
        this.itemNetPrice = itemNetPrice;
    }

    public Integer getIsVirtualBom() {
        return isVirtualBom;
    }

    public void setIsVirtualBom(Integer isVirtualBom) {
        this.isVirtualBom = isVirtualBom;
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

    public String getUserDef1() {
        return userDef1;
    }

    public void setUserDef1(String userDef1) {
        this.userDef1 = userDef1;
    }

    public String getUserDef2() {
        return userDef2;
    }

    public void setUserDef2(String userDef2) {
        this.userDef2 = userDef2;
    }

    public String getUserDef3() {
        return userDef3;
    }

    public void setUserDef3(String userDef3) {
        this.userDef3 = userDef3;
    }

    public String getUserDef4() {
        return userDef4;
    }

    public void setUserDef4(String userDef4) {
        this.userDef4 = userDef4;
    }

    public String getUserDef5() {
        return userDef5;
    }

    public void setUserDef5(String userDef5) {
        this.userDef5 = userDef5;
    }

    public String getUserDef6() {
        return userDef6;
    }

    public void setUserDef6(String userDef6) {
        this.userDef6 = userDef6;
    }

    public String getUserDef7() {
        return userDef7;
    }

    public void setUserDef7(String userDef7) {
        this.userDef7 = userDef7;
    }

    public String getUserDef8() {
        return userDef8;
    }

    public void setUserDef8(String userDef8) {
        this.userDef8 = userDef8;
    }

    public String getProcessStamp() {
        return processStamp;
    }

    public void setProcessStamp(String processStamp) {
        this.processStamp = processStamp;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getStatusFlowCode() {
        return statusFlowCode;
    }

    public void setStatusFlowCode(String statusFlowCode) {
        this.statusFlowCode = statusFlowCode;
    }
}