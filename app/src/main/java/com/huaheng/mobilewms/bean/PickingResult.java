package com.huaheng.mobilewms.bean;

/**
 * Created by youjie on 2018/8/14
 */
public class PickingResult {

    private String message;
    private boolean success;
    private int taskDetailId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTaskDetailId() {
        return taskDetailId;
    }

    public void setTaskDetailId(int taskDetailId) {
        this.taskDetailId = taskDetailId;
    }

    @Override
    public String toString() {
        return "PickingResult{" +
                "message='" + message + '\'' +
                ", success=" + success +
                ", taskDetailId=" + taskDetailId +
                '}';
    }
}
