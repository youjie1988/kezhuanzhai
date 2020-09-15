package com.huaheng.mobilewms.bean;

/**
 * 出库任务创建模型类
 */
public class ShipmentTaskModel {
    /**
     * 需要创建任务的出库货箱列表
     */
    private int[] shipmentContainerHeaderIds;
    /**
     * 是否优先创建整出任务，1，表示可以创建整出就优先创建整出，不能的话就创建拣选出；2，表示只创建拣选出；
     */
    private int taskType;

    /**
     * 优先级
     */
    private short priority;

    public short getPriority() {
        return priority;
    }

    public void setPriority(short priority) {
        this.priority = priority;
    }

    public int[] getShipmentContainerHeaderIds() {
        return shipmentContainerHeaderIds;
    }

    public void setShipmentContainerHeaderIds(int[] shipmentContainerHeaderIds) {
        this.shipmentContainerHeaderIds = shipmentContainerHeaderIds;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }
}
