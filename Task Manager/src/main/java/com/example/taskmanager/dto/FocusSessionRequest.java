package com.example.taskmanager.dto;

public class FocusSessionRequest {

    private Long taskId;
    private Integer duration;

    public FocusSessionRequest() {
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}