package com.huaheng.mobilewms.bean;

import java.math.BigDecimal;

public class MobileInventory {

    /** 库位id   */
    private int id;
    /** 库位编号 */
    private String locationCode;
    /** 容器编号 */
    private String containerCode;
    /** 物料编码 */
    private String materialCode;
    /**物料名称  */
    private String materialName;
    /**物料规格  */
    private String specification;
    /** 数量 */
    private BigDecimal qty;
    /** 任务数量 */
    private BigDecimal taskQty = new BigDecimal(0);
    /** 结果类型 */
    private int resultType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
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

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getTaskQty() {
        return taskQty;
    }

    public void setTaskQty(BigDecimal taskQty) {
        this.taskQty = taskQty;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    @Override
    public String toString() {
        return "MobileInventory{" +
                "id='" + id + '\'' +
                "locationCode='" + locationCode + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", specification='" + specification + '\'' +
                ", qty=" + qty +
                ", taskQty=" + taskQty +
                ", resultType=" + resultType +
                '}';
    }
}
