package com.huaheng.mobilewms.bean;

/**
 * Created by youjie on 2018/8/14
 */
public class Task {
    private String beginTime;
    private int companyId;
    private String containerCode;
    private int containerId;
    private String createdBy;
    private String destinationLocation;
    private String endTime;
    private int id;
    private String lastUpdated;
    private String lastUpdatedBy;
    private int pageNumber;
    private int pageSize;
    private int priority;
    private String sectionEndTime;
    private String sectionStartTime;
    private String sourceLocation;
    private int station;
    private int status;
    private int type;
    private String userDef1;
    private String userDef2;
    private String userDef3;
    private String warehouseCode;
    private int warehouseId;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getSectionEndTime() {
        return sectionEndTime;
    }

    public void setSectionEndTime(String sectionEndTime) {
        this.sectionEndTime = sectionEndTime;
    }

    public String getSectionStartTime() {
        return sectionStartTime;
    }

    public void setSectionStartTime(String sectionStartTime) {
        this.sectionStartTime = sectionStartTime;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "beginTime='" + beginTime + '\'' +
                ", companyId=" + companyId +
                ", containerCode='" + containerCode + '\'' +
                ", containerId=" + containerId +
                ", createdBy='" + createdBy + '\'' +
                ", destinationLocation='" + destinationLocation + '\'' +
                ", endTime='" + endTime + '\'' +
                ", id=" + id +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", priority=" + priority +
                ", sectionEndTime='" + sectionEndTime + '\'' +
                ", sectionStartTime='" + sectionStartTime + '\'' +
                ", sourceLocation='" + sourceLocation + '\'' +
                ", station=" + station +
                ", status=" + status +
                ", type=" + type +
                ", userDef1='" + userDef1 + '\'' +
                ", userDef2='" + userDef2 + '\'' +
                ", userDef3='" + userDef3 + '\'' +
                ", warehouseCode=" + warehouseCode +
                ", warehouseId=" + warehouseId +
                '}';
    }
}
