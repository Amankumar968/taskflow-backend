package com.example.taskmanager.dto;

import java.util.List;
import java.util.Map;

public class DashboardResponse {

    private int totalFocusMinutesToday;
    private int completedSessionsToday;
    private long pendingTasks;
    private long completedTasks;
    private Map<String, Integer> weeklyFocusMinutes;
    private List<FocusSessionResponse> recentSessions;

    public DashboardResponse() {
    }

    public DashboardResponse(int totalFocusMinutesToday, int completedSessionsToday,
                             long pendingTasks, long completedTasks,
                             Map<String, Integer> weeklyFocusMinutes,
                             List<FocusSessionResponse> recentSessions) {
        this.totalFocusMinutesToday = totalFocusMinutesToday;
        this.completedSessionsToday = completedSessionsToday;
        this.pendingTasks = pendingTasks;
        this.completedTasks = completedTasks;
        this.weeklyFocusMinutes = weeklyFocusMinutes;
        this.recentSessions = recentSessions;
    }

    public int getTotalFocusMinutesToday() {
        return totalFocusMinutesToday;
    }

    public void setTotalFocusMinutesToday(int totalFocusMinutesToday) {
        this.totalFocusMinutesToday = totalFocusMinutesToday;
    }

    public int getCompletedSessionsToday() {
        return completedSessionsToday;
    }

    public void setCompletedSessionsToday(int completedSessionsToday) {
        this.completedSessionsToday = completedSessionsToday;
    }

    public long getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(long pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public Map<String, Integer> getWeeklyFocusMinutes() {
        return weeklyFocusMinutes;
    }

    public void setWeeklyFocusMinutes(Map<String, Integer> weeklyFocusMinutes) {
        this.weeklyFocusMinutes = weeklyFocusMinutes;
    }

    public List<FocusSessionResponse> getRecentSessions() {
        return recentSessions;
    }

    public void setRecentSessions(List<FocusSessionResponse> recentSessions) {
        this.recentSessions = recentSessions;
    }
}