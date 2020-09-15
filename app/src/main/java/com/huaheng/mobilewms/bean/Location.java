package com.huaheng.mobilewms.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Location {

    private Integer id;
    private String code;
    private String warehouseCode;
    private String zoneCode;
    private String locationType;
    private String containerCode;
    private Integer iRow;
    private Integer iColumn;
    private Integer iLayer;
    private Integer iGrid;
    private Integer rowFlag;
    private String roadway;
    private String name;
    private String status;
    private String lastCycleCountDate;
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
    private Integer systemCreated;
    private Boolean deleted;
    private List<String> materialCode;
    private List<String> materialName;
    private List<String> batch;
    private List<BigDecimal> qty;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public Integer getiRow() {
        return iRow;
    }

    public void setiRow(Integer iRow) {
        this.iRow = iRow;
    }

    public Integer getiColumn() {
        return iColumn;
    }

    public void setiColumn(Integer iColumn) {
        this.iColumn = iColumn;
    }

    public Integer getiLayer() {
        return iLayer;
    }

    public void setiLayer(Integer iLayer) {
        this.iLayer = iLayer;
    }

    public Integer getiGrid() {
        return iGrid;
    }

    public void setiGrid(Integer iGrid) {
        this.iGrid = iGrid;
    }

    public Integer getRowFlag() {
        return rowFlag;
    }

    public void setRowFlag(Integer rowFlag) {
        this.rowFlag = rowFlag;
    }

    public String getRoadway() {
        return roadway;
    }

    public void setRoadway(String roadway) {
        this.roadway = roadway;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastCycleCountDate() {
        return lastCycleCountDate;
    }

    public void setLastCycleCountDate(String lastCycleCountDate) {
        this.lastCycleCountDate = lastCycleCountDate;
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

    public Integer getSystemCreated() {
        return systemCreated;
    }

    public void setSystemCreated(Integer systemCreated) {
        this.systemCreated = systemCreated;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List <String> getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(List <String> materialCode) {
        this.materialCode = materialCode;
    }

    public List <String> getMaterialName() {
        return materialName;
    }

    public void setMaterialName(List <String> materialName) {
        this.materialName = materialName;
    }

    public List <String> getBatch() {
        return batch;
    }

    public void setBatch(List <String> batch) {
        this.batch = batch;
    }

    public List <BigDecimal> getQty() {
        return qty;
    }

    public void setQty(List <BigDecimal> qty) {
        this.qty = qty;
    }
}