package com.huaheng.mobilewms.bean;


import java.util.List;

/**
 * Created by youjie on 2018/8/14
 */
public class TaskDetailBean {

    private Task task;
    private List<TaskDetail> taskDetails;
    private List<TaskStatusConfig> taskStatusConfig;
    private List<TaskStatusConfig> taskDetailStatusConfig;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<TaskDetail> getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(List<TaskDetail> taskDetails) {
        this.taskDetails = taskDetails;
    }

    public List<TaskStatusConfig> getTaskStatusConfig() {
        return taskStatusConfig;
    }

    public void setTaskStatusConfig(List<TaskStatusConfig> taskStatusConfig) {
        this.taskStatusConfig = taskStatusConfig;
    }

    public List<TaskStatusConfig> getTaskDetailStatusConfig() {
        return taskDetailStatusConfig;
    }

    public void setTaskDetailStatusConfig(List<TaskStatusConfig> taskDetailStatusConfig) {
        this.taskDetailStatusConfig = taskDetailStatusConfig;
    }

    @Override
    public String toString() {
        return "TaskDetailBean{" +
                "task=" + task +
                ", taskDetails=" + taskDetails +
                ", taskStatusConfig=" + taskStatusConfig +
                ", taskDetailStatusConfig=" + taskDetailStatusConfig +
                '}';
    }
}
