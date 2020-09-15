package com.huaheng.mobilewms.bean;

import java.io.Serializable;

public class TaskHeader {

    private Integer id;
    private String warehouseCode;
    private String companyCode;
    private Integer taskType;
    private Integer allocationHeadId;
    private Integer internalTaskType;
    private String containerCode;
    private String fromLocation;
    private String toLocation;
    private Integer referenceId;
    private String referenceCode;
    private String assignedUser;
    private String confirmedBy;
    private Integer status;
    private Integer waveId;
    private String pickingCartCode;
    private String pickingCartPos;
    private String startPickDateTime;
    private String endPickDateTime;
    private String rebatchLoc;
    private Integer finishRebatch;
    private String rebatchGroupCode;
    private Integer allowRebatch;
    private String taskProcessType;
    private String rebinBench;
    private Integer rebined;
    private String startRebinDateTime;
    private String endRebinDateTime;
    private String rebinedBy;
    private String exceptionCode;
    private String exceptionHandledBy;
    private String created;
    private String createdBy;
    private String lastUpdated;
    private String lastUpdatedBy;
    private Integer version;
    private String userDef1;
    private String userDef2;
    private String userDef3;
    private String processStamp;
    private String recvDock;
    private Integer containQty;
    private String weight;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getAllocationHeadId() {
        return allocationHeadId;
    }

    public void setAllocationHeadId(Integer allocationHeadId) {
        this.allocationHeadId = allocationHeadId;
    }

    public Integer getInternalTaskType() {
        return internalTaskType;
    }

    public void setInternalTaskType(Integer internalTaskType) {
        this.internalTaskType = internalTaskType;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }

    public String getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getWaveId() {
        return waveId;
    }

    public void setWaveId(Integer waveId) {
        this.waveId = waveId;
    }

    public String getPickingCartCode() {
        return pickingCartCode;
    }

    public void setPickingCartCode(String pickingCartCode) {
        this.pickingCartCode = pickingCartCode;
    }

    public String getPickingCartPos() {
        return pickingCartPos;
    }

    public void setPickingCartPos(String pickingCartPos) {
        this.pickingCartPos = pickingCartPos;
    }

    public String getStartPickDateTime() {
        return startPickDateTime;
    }

    public void setStartPickDateTime(String startPickDateTime) {
        this.startPickDateTime = startPickDateTime;
    }

    public String getEndPickDateTime() {
        return endPickDateTime;
    }

    public void setEndPickDateTime(String endPickDateTime) {
        this.endPickDateTime = endPickDateTime;
    }

    public String getRebatchLoc() {
        return rebatchLoc;
    }

    public void setRebatchLoc(String rebatchLoc) {
        this.rebatchLoc = rebatchLoc;
    }

    public Integer getFinishRebatch() {
        return finishRebatch;
    }

    public void setFinishRebatch(Integer finishRebatch) {
        this.finishRebatch = finishRebatch;
    }

    public String getRebatchGroupCode() {
        return rebatchGroupCode;
    }

    public void setRebatchGroupCode(String rebatchGroupCode) {
        this.rebatchGroupCode = rebatchGroupCode;
    }

    public Integer getAllowRebatch() {
        return allowRebatch;
    }

    public void setAllowRebatch(Integer allowRebatch) {
        this.allowRebatch = allowRebatch;
    }

    public String getTaskProcessType() {
        return taskProcessType;
    }

    public void setTaskProcessType(String taskProcessType) {
        this.taskProcessType = taskProcessType;
    }

    public String getRebinBench() {
        return rebinBench;
    }

    public void setRebinBench(String rebinBench) {
        this.rebinBench = rebinBench;
    }

    public Integer getRebined() {
        return rebined;
    }

    public void setRebined(Integer rebined) {
        this.rebined = rebined;
    }

    public String getStartRebinDateTime() {
        return startRebinDateTime;
    }

    public void setStartRebinDateTime(String startRebinDateTime) {
        this.startRebinDateTime = startRebinDateTime;
    }

    public String getEndRebinDateTime() {
        return endRebinDateTime;
    }

    public void setEndRebinDateTime(String endRebinDateTime) {
        this.endRebinDateTime = endRebinDateTime;
    }

    public String getRebinedBy() {
        return rebinedBy;
    }

    public void setRebinedBy(String rebinedBy) {
        this.rebinedBy = rebinedBy;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionHandledBy() {
        return exceptionHandledBy;
    }

    public void setExceptionHandledBy(String exceptionHandledBy) {
        this.exceptionHandledBy = exceptionHandledBy;
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

    public String getProcessStamp() {
        return processStamp;
    }

    public void setProcessStamp(String processStamp) {
        this.processStamp = processStamp;
    }

    public String getRecvDock() {
        return recvDock;
    }

    public void setRecvDock(String recvDock) {
        this.recvDock = recvDock;
    }

    public Integer getContainQty() {
        return containQty;
    }

    public void setContainQty(Integer containQty) {
        this.containQty = containQty;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}