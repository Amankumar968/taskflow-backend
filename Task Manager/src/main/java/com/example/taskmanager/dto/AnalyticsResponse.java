package com.example.taskmanager.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsResponse {

    private Map<String, Long> taskCompletionTrend;      // date -> completed count (last 30 days)
    private Map<String, Integer> focusTimeTrend;         // week label -> total minutes (last 8 weeks)
    private Map<String, Long> categoryBreakdown;         // category -> task count
    private Map<String, Long> priorityBreakdown;         // priority -> task count
    private List<HeatmapCell> productivityHeatmap;       // day + time-block -> minutes
    private int currentStreak;
    private int longestStreak;

    public AnalyticsResponse() {
    }

    public AnalyticsResponse(Map<String, Long> taskCompletionTrend,
                             Map<String, Integer> focusTimeTrend,
                             Map<String, Long> categoryBreakdown,
                             Map<String, Long> priorityBreakdown,
                             List<HeatmapCell> productivityHeatmap,
                             int currentStreak,
                             int longestStreak) {
        this.taskCompletionTrend = taskCompletionTrend;
        this.focusTimeTrend = focusTimeTrend;
        this.categoryBreakdown = categoryBreakdown;
        this.priorityBreakdown = priorityBreakdown;
        this.productivityHeatmap = productivityHeatmap;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
    }

    public Map<String, Long> getTaskCompletionTrend() { return taskCompletionTrend; }
    public void setTaskCompletionTrend(Map<String, Long> taskCompletionTrend) { this.taskCompletionTrend = taskCompletionTrend; }

    public Map<String, Integer> getFocusTimeTrend() { return focusTimeTrend; }
    public void setFocusTimeTrend(Map<String, Integer> focusTimeTrend) { this.focusTimeTrend = focusTimeTrend; }

    public Map<String, Long> getCategoryBreakdown() { return categoryBreakdown; }
    public void setCategoryBreakdown(Map<String, Long> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; }

    public Map<String, Long> getPriorityBreakdown() { return priorityBreakdown; }
    public void setPriorityBreakdown(Map<String, Long> priorityBreakdown) { this.priorityBreakdown = priorityBreakdown; }

    public List<HeatmapCell> getProductivityHeatmap() { return productivityHeatmap; }
    public void setProductivityHeatmap(List<HeatmapCell> productivityHeatmap) { this.productivityHeatmap = productivityHeatmap; }

    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }

    public int getLongestStreak() { return longestStreak; }
    public void setLongestStreak(int longestStreak) { this.longestStreak = longestStreak; }

    public static class HeatmapCell {
        private String day;
        private String timeBlock;
        private int minutes;

        public HeatmapCell() {
        }

        public HeatmapCell(String day, String timeBlock, int minutes) {
            this.day = day;
            this.timeBlock = timeBlock;
            this.minutes = minutes;
        }

        public String getDay() { return day; }
        public void setDay(String day) { this.day = day; }

        public String getTimeBlock() { return timeBlock; }
        public void setTimeBlock(String timeBlock) { this.timeBlock = timeBlock; }

        public int getMinutes() { return minutes; }
        public void setMinutes(int minutes) { this.minutes = minutes; }
    }
}