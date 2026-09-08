//package com.example.taskmanager.service;
//
//import com.example.taskmanager.dto.AnalyticsResponse;
//import com.example.taskmanager.model.FocusSession;
//import com.example.taskmanager.model.FocusSessionStatus;
//import com.example.taskmanager.model.Task;
//import com.example.taskmanager.model.User;
//import com.example.taskmanager.repository.FocusSessionRepository;
//import com.example.taskmanager.repository.TaskRepository;
//import com.example.taskmanager.repository.UserRepository;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class AnalyticsService {
//
//    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d");
//
//    private final TaskRepository taskRepository;
//    private final FocusSessionRepository focusSessionRepository;
//    private final UserRepository userRepository;
//
//    public AnalyticsService(TaskRepository taskRepository,
//                            FocusSessionRepository focusSessionRepository,
//                            UserRepository userRepository) {
//        this.taskRepository = taskRepository;
//        this.focusSessionRepository = focusSessionRepository;
//        this.userRepository = userRepository;
//    }
//
//    private User getAuthenticatedUser() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
//    }
//
//    public AnalyticsResponse getAnalytics() {
//        User user = getAuthenticatedUser();
//
//        List<Task> tasks = taskRepository.findByUser(user);
//        List<FocusSession> completedSessions = focusSessionRepository
//                .findByUserIdAndStatus(user.getId(), FocusSessionStatus.COMPLETED);
//
//        return new AnalyticsResponse(
//                buildTaskCompletionTrend(tasks, 30),
//                buildFocusTimeTrend(completedSessions, 8),
//                buildCategoryBreakdown(tasks),
//                buildPriorityBreakdown(tasks),
//                buildHeatmap(completedSessions),
//                calculateCurrentStreak(tasks),
//                calculateLongestStreak(tasks)
//        );
//    }
//
//    private Map<String, Long> buildTaskCompletionTrend(List<Task> tasks, int days) {
//        LocalDate today = LocalDate.now();
//        LocalDate start = today.minusDays(days - 1);
//
//        Map<String, Long> trend = new LinkedHashMap<>();
//        for (LocalDate d = start; !d.isAfter(today); d = d.plusDays(1)) {
//            trend.put(d.format(DATE_FMT), 0L);
//        }
//
//        Map<LocalDate, Long> counts = tasks.stream()
//                .filter(t -> t.getCompletedAt() != null)
//                .filter(t -> !t.getCompletedAt().toLocalDate().isBefore(start))
//                .collect(Collectors.groupingBy(t -> t.getCompletedAt().toLocalDate(), Collectors.counting()));
//
//        counts.forEach((date, count) -> trend.put(date.format(DATE_FMT), count));
//        return trend;
//    }
//
//    private Map<String, Integer> buildFocusTimeTrend(List<FocusSession> sessions, int weeks) {
//        LocalDate today = LocalDate.now();
//        LocalDate start = today.minusWeeks(weeks - 1).with(DayOfWeek.MONDAY);
//
//        Map<String, Integer> trend = new LinkedHashMap<>();
//        for (LocalDate d = start; !d.isAfter(today); d = d.plusWeeks(1)) {
//            trend.put("Wk " + d.format(DATE_FMT), 0);
//        }
//
//        for (FocusSession s : sessions) {
//            if (s.getStartedAt() == null) continue;
//            LocalDate sessionDate = s.getStartedAt().toLocalDate();
//            if (sessionDate.isBefore(start)) continue;
//
//            LocalDate weekStart = sessionDate.with(DayOfWeek.MONDAY);
//            String label = "Wk " + weekStart.format(DATE_FMT);
//            trend.merge(label, s.getDuration(), Integer::sum);
//        }
//        return trend;
//    }
//
//    private Map<String, Long> buildCategoryBreakdown(List<Task> tasks) {
//        return tasks.stream()
//                .collect(Collectors.groupingBy(
//                        t -> t.getCategory() != null && !t.getCategory().isBlank() ? t.getCategory() : "Uncategorized",
//                        Collectors.counting()));
//    }
//
//    private Map<String, Long> buildPriorityBreakdown(List<Task> tasks) {
//        return tasks.stream()
//                .collect(Collectors.groupingBy(
//                        t -> t.getPriority() != null ? t.getPriority().name() : "NONE",
//                        Collectors.counting()));
//    }
//
//    private List<AnalyticsResponse.HeatmapCell> buildHeatmap(List<FocusSession> sessions) {
//        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
//        String[] blocks = {"Night", "Morning", "Afternoon", "Evening"};
//
//        Map<String, Integer> grid = new LinkedHashMap<>();
//        for (String d : days) for (String b : blocks) grid.put(d + "|" + b, 0);
//
//        for (FocusSession s : sessions) {
//            if (s.getStartedAt() == null) continue;
//            String day = days[s.getStartedAt().getDayOfWeek().getValue() - 1];
//            int hour = s.getStartedAt().getHour();
//            String block = hour < 6 ? "Night" : hour < 12 ? "Morning" : hour < 18 ? "Afternoon" : "Evening";
//            grid.merge(day + "|" + block, s.getDuration(), Integer::sum);
//        }
//
//        List<AnalyticsResponse.HeatmapCell> cells = new ArrayList<>();
//        grid.forEach((key, minutes) -> {
//            String[] parts = key.split("\\|");
//            cells.add(new AnalyticsResponse.HeatmapCell(parts[0], parts[1], minutes));
//        });
//        return cells;
//    }
//
//    private int calculateCurrentStreak(List<Task> tasks) {
//        Set<LocalDate> completedDates = tasks.stream()
//                .filter(t -> t.getCompletedAt() != null)
//                .map(t -> t.getCompletedAt().toLocalDate())
//                .collect(Collectors.toSet());
//
//        int streak = 0;
//        LocalDate day = LocalDate.now();
//        if (!completedDates.contains(day)) day = day.minusDays(1);
//        while (completedDates.contains(day)) {
//            streak++;
//            day = day.minusDays(1);
//        }
//        return streak;
//    }
//
//    private int calculateLongestStreak(List<Task> tasks) {
//        TreeSet<LocalDate> completedDates = tasks.stream()
//                .filter(t -> t.getCompletedAt() != null)
//                .map(t -> t.getCompletedAt().toLocalDate())
//                .collect(Collectors.toCollection(TreeSet::new));
//
//        int longest = 0, current = 0;
//        LocalDate prev = null;
//        for (LocalDate d : completedDates) {
//            current = (prev != null && d.equals(prev.plusDays(1))) ? current + 1 : 1;
//            longest = Math.max(longest, current);
//            prev = d;
//        }
//        return longest;
//    }
//}

package com.example.taskmanager.service;

import com.example.taskmanager.dto.AnalyticsResponse;
import com.example.taskmanager.model.FocusSession;
import com.example.taskmanager.model.FocusSessionStatus;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.FocusSessionRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d");

    private final TaskRepository taskRepository;
    private final FocusSessionRepository focusSessionRepository;
    private final UserRepository userRepository;
    private final FocusSessionService focusSessionService;

    public AnalyticsService(TaskRepository taskRepository,
                            FocusSessionRepository focusSessionRepository,
                            UserRepository userRepository,
                            FocusSessionService focusSessionService) {
        this.taskRepository = taskRepository;
        this.focusSessionRepository = focusSessionRepository;
        this.userRepository = userRepository;
        this.focusSessionService = focusSessionService;
    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    public AnalyticsResponse getAnalytics() {
        User user = getAuthenticatedUser();

        // Keep this in sync with Dashboard/Focus Timer: a session left
        // RUNNING past its scheduled end time should count as completed
        // before we compute any trends from it.
        focusSessionService.resolveStaleRunningSessions(user.getId());

        List<Task> tasks = taskRepository.findByUser(user);
        List<FocusSession> completedSessions = focusSessionRepository
                .findByUserIdAndStatus(user.getId(), FocusSessionStatus.COMPLETED);

        return new AnalyticsResponse(
                buildTaskCompletionTrend(tasks, 30),
                buildFocusTimeTrend(completedSessions, 8),
                buildCategoryBreakdown(tasks),
                buildPriorityBreakdown(tasks),
                buildHeatmap(completedSessions),
                calculateCurrentStreak(tasks),
                calculateLongestStreak(tasks)
        );
    }

    private Map<String, Long> buildTaskCompletionTrend(List<Task> tasks, int days) {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(days - 1);

        Map<String, Long> trend = new LinkedHashMap<>();
        for (LocalDate d = start; !d.isAfter(today); d = d.plusDays(1)) {
            trend.put(d.format(DATE_FMT), 0L);
        }

        Map<LocalDate, Long> counts = tasks.stream()
                .filter(t -> t.getCompletedAt() != null)
                .filter(t -> !t.getCompletedAt().toLocalDate().isBefore(start))
                .collect(Collectors.groupingBy(t -> t.getCompletedAt().toLocalDate(), Collectors.counting()));

        counts.forEach((date, count) -> trend.put(date.format(DATE_FMT), count));
        return trend;
    }

    private Map<String, Integer> buildFocusTimeTrend(List<FocusSession> sessions, int weeks) {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusWeeks(weeks - 1).with(DayOfWeek.MONDAY);

        Map<String, Integer> trend = new LinkedHashMap<>();
        for (LocalDate d = start; !d.isAfter(today); d = d.plusWeeks(1)) {
            trend.put("Wk " + d.format(DATE_FMT), 0);
        }

        for (FocusSession s : sessions) {
            if (s.getStartedAt() == null) continue;
            LocalDate sessionDate = s.getStartedAt().toLocalDate();
            if (sessionDate.isBefore(start)) continue;

            LocalDate weekStart = sessionDate.with(DayOfWeek.MONDAY);
            String label = "Wk " + weekStart.format(DATE_FMT);
            trend.merge(label, s.getDuration(), Integer::sum);
        }
        return trend;
    }

    private Map<String, Long> buildCategoryBreakdown(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory() != null && !t.getCategory().isBlank() ? t.getCategory() : "Uncategorized",
                        Collectors.counting()));
    }

    private Map<String, Long> buildPriorityBreakdown(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getPriority() != null ? t.getPriority().name() : "NONE",
                        Collectors.counting()));
    }

    private List<AnalyticsResponse.HeatmapCell> buildHeatmap(List<FocusSession> sessions) {
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        String[] blocks = {"Night", "Morning", "Afternoon", "Evening"};

        Map<String, Integer> grid = new LinkedHashMap<>();
        for (String d : days) for (String b : blocks) grid.put(d + "|" + b, 0);

        for (FocusSession s : sessions) {
            if (s.getStartedAt() == null) continue;
            String day = days[s.getStartedAt().getDayOfWeek().getValue() - 1];
            int hour = s.getStartedAt().getHour();
            String block = hour < 6 ? "Night" : hour < 12 ? "Morning" : hour < 18 ? "Afternoon" : "Evening";
            grid.merge(day + "|" + block, s.getDuration(), Integer::sum);
        }

        List<AnalyticsResponse.HeatmapCell> cells = new ArrayList<>();
        grid.forEach((key, minutes) -> {
            String[] parts = key.split("\\|");
            cells.add(new AnalyticsResponse.HeatmapCell(parts[0], parts[1], minutes));
        });
        return cells;
    }

    private int calculateCurrentStreak(List<Task> tasks) {
        Set<LocalDate> completedDates = tasks.stream()
                .filter(t -> t.getCompletedAt() != null)
                .map(t -> t.getCompletedAt().toLocalDate())
                .collect(Collectors.toSet());

        int streak = 0;
        LocalDate day = LocalDate.now();
        if (!completedDates.contains(day)) day = day.minusDays(1);
        while (completedDates.contains(day)) {
            streak++;
            day = day.minusDays(1);
        }
        return streak;
    }

    private int calculateLongestStreak(List<Task> tasks) {
        TreeSet<LocalDate> completedDates = tasks.stream()
                .filter(t -> t.getCompletedAt() != null)
                .map(t -> t.getCompletedAt().toLocalDate())
                .collect(Collectors.toCollection(TreeSet::new));

        int longest = 0, current = 0;
        LocalDate prev = null;
        for (LocalDate d : completedDates) {
            current = (prev != null && d.equals(prev.plusDays(1))) ? current + 1 : 1;
            longest = Math.max(longest, current);
            prev = d;
        }
        return longest;
    }
}