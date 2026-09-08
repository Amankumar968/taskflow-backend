//package com.example.taskmanager.model;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//public class FocusSession {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "task_id")
//    private Task task;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    private LocalDateTime startedAt;
//    private LocalDateTime completedAt;
//    private int duration; // minutes
//
//    @Enumerated(EnumType.STRING)
//    private FocusSessionStatus status;
//
//    public FocusSession() {
//    }
//
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public Task getTask() { return task; }
//    public void setTask(Task task) { this.task = task; }
//
//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }
//
//    public LocalDateTime getStartedAt() { return startedAt; }
//    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
//
//    public LocalDateTime getCompletedAt() { return completedAt; }
//    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
//
//    public int getDuration() { return duration; }
//    public void setDuration(int duration) { this.duration = duration; }
//
//    public FocusSessionStatus getStatus() { return status; }
//    public void setStatus(FocusSessionStatus status) { this.status = status; }
//}


package com.example.taskmanager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class FocusSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private int duration; // minutes

    @Enumerated(EnumType.STRING)
    private FocusSessionStatus status;

    public FocusSession() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public FocusSessionStatus getStatus() { return status; }
    public void setStatus(FocusSessionStatus status) { this.status = status; }
}