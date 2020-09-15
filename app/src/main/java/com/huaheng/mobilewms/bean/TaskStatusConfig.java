package com.huaheng.mobilewms.bean;

/**
 * Created by youjie on 2018/8/14
 */
public class TaskStatusConfig {

    private String code;
    private String created;
    private String createdBy;
    private boolean deleted;
    private String description;
    private boolean enable;
    private int headerId;
    private int id;
    private String lastUpdated;
    private String lastUpdatedBy;
    private int pageNumber;
    private int pageSize;
    private String sectionEndTime;
    private String sectionStartTime;
    private String userDef1;
    private String userDef2;
    private String userDef3;
    private String userDef4;
    private String userDef5;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
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

    @Override
    public String toString() {
        return "TaskStatusConfig{" +
                "code='" + code + '\'' +
                ", created='" + created + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", deleted=" + deleted +
                ", description='" + description + '\'' +
                ", enable=" + enable +
                ", headerId=" + headerId +
                ", id=" + id +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", sectionEndTime='" + sectionEndTime + '\'' +
                ", sectionStartTime='" + sectionStartTime + '\'' +
                ", userDef1='" + userDef1 + '\'' +
                ", userDef2='" + userDef2 + '\'' +
                ", userDef3='" + userDef3 + '\'' +
                ", userDef4='" + userDef4 + '\'' +
                ", userDef5='" + userDef5 + '\'' +
                '}';
    }
}
