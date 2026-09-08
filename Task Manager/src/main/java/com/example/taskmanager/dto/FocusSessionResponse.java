//package com.example.taskmanager.dto;
//
//import com.example.taskmanager.model.FocusSessionStatus;
//
//import java.time.LocalDateTime;
//
//public class FocusSessionResponse {
//
//    private Long id;
//    private Long taskId;
//    private String taskTitle;
//    private Integer duration;
//    private LocalDateTime startedAt;
//    private LocalDateTime completedAt;
//    private FocusSessionStatus status;
//
//    public FocusSessionResponse() {
//    }
//
//    public FocusSessionResponse(Long id, Long taskId, String taskTitle, Integer duration,
//                                LocalDateTime startedAt, LocalDateTime completedAt,
//                                FocusSessionStatus status) {
//        this.id = id;
//        this.taskId = taskId;
//        this.taskTitle = taskTitle;
//        this.duration = duration;
//        this.startedAt = startedAt;
//        this.completedAt = completedAt;
//        this.status = status;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getTaskId() {
//        return taskId;
//    }
//
//    public void setTaskId(Long taskId) {
//        this.taskId = taskId;
//    }
//
//    public String getTaskTitle() {
//        return taskTitle;
//    }
//
//    public void setTaskTitle(String taskTitle) {
//        this.taskTitle = taskTitle;
//    }
//
//    public Integer getDuration() {
//        return duration;
//    }
//
//    public void setDuration(Integer duration) {
//        this.duration = duration;
//    }
//
//    public LocalDateTime getStartedAt() {
//        return startedAt;
//    }
//
//    public void setStartedAt(LocalDateTime startedAt) {
//        this.startedAt = startedAt;
//    }
//
//    public LocalDateTime getCompletedAt() {
//        return completedAt;
//    }
//
//    public void setCompletedAt(LocalDateTime completedAt) {
//        this.completedAt = completedAt;
//    }
//
//    public FocusSessionStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(FocusSessionStatus status) {
//        this.status = status;
//    }
//}

package com.example.taskmanager.dto;

import com.example.taskmanager.model.FocusSessionStatus;

import java.time.LocalDateTime;

public class FocusSessionResponse {

    private Long id;
    private Long taskId;
    private String taskTitle;
    private Integer duration;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private FocusSessionStatus status;

    public FocusSessionResponse() {
    }

    public FocusSessionResponse(Long id, Long taskId, String taskTitle, Integer duration,
                                LocalDateTime startedAt, LocalDateTime completedAt,
                                LocalDateTime cancelledAt, FocusSessionStatus status) {
        this.id = id;
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.duration = duration;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.cancelledAt = cancelledAt;
        this.status = status;
    }

    public FocusSessionResponse(Long id, Long aLong, String s, int duration, LocalDateTime startedAt, LocalDateTime completedAt, FocusSessionStatus status) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public FocusSessionStatus getStatus() {
        return status;
    }

    public void setStatus(FocusSessionStatus status) {
        this.status = status;
    }
}