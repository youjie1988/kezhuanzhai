package com.huaheng.mobilewms.bean;

import java.util.List;

/**
 *
 * @author Enzo Cotter
 * @date 2020/1/3
 */
public class MobileTask {

    private TaskHeader taskHeader;

    private List<TaskDetail> taskDetail;

    public TaskHeader getTaskHeader() {
        return taskHeader;
    }

    public void setTaskHeader(TaskHeader taskHeader) {
        this.taskHeader = taskHeader;
    }

    public List <TaskDetail> getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(List <TaskDetail> taskDetail) {
        this.taskDetail = taskDetail;
    }
}
