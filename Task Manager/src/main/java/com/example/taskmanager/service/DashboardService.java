////package com.example.taskmanager.service;
////
////import com.example.taskmanager.dto.DashboardResponse;
////import com.example.taskmanager.dto.FocusSessionResponse;
////import com.example.taskmanager.model.FocusSession;
////import com.example.taskmanager.model.FocusSessionStatus;
////import com.example.taskmanager.model.User;
////import com.example.taskmanager.repository.FocusSessionRepository;
////import com.example.taskmanager.repository.TaskRepository;
////import com.example.taskmanager.repository.UserRepository;
////import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.stereotype.Service;
////
////import java.time.DayOfWeek;
////import java.time.LocalDate;
////import java.time.LocalDateTime;
////import java.time.format.TextStyle;
////import java.util.LinkedHashMap;
////import java.util.List;
////import java.util.Locale;
////import java.util.Map;
////import java.util.stream.Collectors;
////
////@Service
////public class DashboardService {
////
////    private final FocusSessionRepository focusSessionRepository;
////    private final TaskRepository taskRepository;
////    private final UserRepository userRepository;
////
////    public DashboardService(FocusSessionRepository focusSessionRepository,
////                            TaskRepository taskRepository,
////                            UserRepository userRepository) {
////        this.focusSessionRepository = focusSessionRepository;
////        this.taskRepository = taskRepository;
////        this.userRepository = userRepository;
////    }
////
////    private User getAuthenticatedUser() {
////        String username = SecurityContextHolder.getContext().getAuthentication().getName();
////        return userRepository.findByUsername(username)
////                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
////    }
////
////    public DashboardResponse getDashboard() {
////        User user = getAuthenticatedUser();
////
////        // ---- Today's focus stats ----
////        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
////        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
////
////        List<FocusSession> todaySessions = focusSessionRepository
////                .findByUserIdAndStartedAtBetween(user.getId(), startOfDay, endOfDay);
////
////        int totalFocusMinutesToday = todaySessions.stream()
////                .filter(s -> s.getStatus() == FocusSessionStatus.COMPLETED)
////                .mapToInt(FocusSession::getDuration)
////                .sum();
////
////        int completedSessionsToday = (int) todaySessions.stream()
////                .filter(s -> s.getStatus() == FocusSessionStatus.COMPLETED)
////                .count();
////
////        // ---- Task counts ----
////        long pendingTasks = taskRepository.countByUserAndCompleted(user, false);
////        long completedTasks = taskRepository.countByUserAndCompleted(user, true);
////
////        // ---- Weekly focus chart (last 7 days, including today) ----
////        Map<String, Integer> weeklyFocusMinutes = new LinkedHashMap<>();
////
////        for (int i = 6; i >= 0; i--) {
////            LocalDate date = LocalDate.now().minusDays(i);
////            LocalDateTime dayStart = date.atStartOfDay();
////            LocalDateTime dayEnd = date.atTime(23, 59, 59);
////
////            List<FocusSession> daySessions = focusSessionRepository
////                    .findByUserIdAndStartedAtBetweenAndStatus(
////                            user.getId(), dayStart, dayEnd, FocusSessionStatus.COMPLETED);
////
////            int minutes = daySessions.stream()
////                    .mapToInt(FocusSession::getDuration)
////                    .sum();
////
////            String dayLabel = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
////            weeklyFocusMinutes.put(dayLabel, minutes);
////        }
////
////        // ---- Recent sessions (latest 5) ----
////        List<FocusSessionResponse> recentSessions = focusSessionRepository
////                .findTop5ByUserIdOrderByStartedAtDesc(user.getId())
////                .stream()
////                .map(this::toResponse)
////                .collect(Collectors.toList());
////
////        return new DashboardResponse(
////                totalFocusMinutesToday,
////                completedSessionsToday,
////                pendingTasks,
////                completedTasks,
////                weeklyFocusMinutes,
////                recentSessions
////        );
////    }
////
////    private FocusSessionResponse toResponse(FocusSession session) {
////        return new FocusSessionResponse(
////                session.getId(),
////                session.getTask() != null ? session.getTask().getId() : null,
////                session.getTask() != null ? session.getTask().getTitle() : null,
////                session.getDuration(),
////                session.getStartedAt(),
////                session.getCompletedAt(),
////                session.getStatus()
////        );
////    }
////}
//
//
//package com.example.taskmanager.service;
//
//import com.example.taskmanager.dto.DashboardResponse;
//import com.example.taskmanager.dto.FocusSessionResponse;
//import com.example.taskmanager.model.FocusSession;
//import com.example.taskmanager.model.FocusSessionStatus;
//import com.example.taskmanager.model.User;
//import com.example.taskmanager.repository.FocusSessionRepository;
//import com.example.taskmanager.repository.TaskRepository;
//import com.example.taskmanager.repository.UserRepository;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.TextStyle;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class DashboardService {
//
//    private final FocusSessionRepository focusSessionRepository;
//    private final TaskRepository taskRepository;
//    private final UserRepository userRepository;
//
//    public DashboardService(FocusSessionRepository focusSessionRepository,
//                            TaskRepository taskRepository,
//                            UserRepository userRepository) {
//        this.focusSessionRepository = focusSessionRepository;
//        this.taskRepository = taskRepository;
//        this.userRepository = userRepository;
//    }
//
//    private User getAuthenticatedUser() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
//    }
//
//    public DashboardResponse getDashboard() {
//        User user = getAuthenticatedUser();
//
//        // ---- Today's focus stats ----
//        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
//        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
//
//        List<FocusSession> todaySessions = focusSessionRepository
//                .findByUserIdAndStartedAtBetween(user.getId(), startOfDay, endOfDay);
//
//        int totalFocusMinutesToday = todaySessions.stream()
//                .filter(s -> s.getStatus() == FocusSessionStatus.COMPLETED)
//                .mapToInt(FocusSession::getDuration)
//                .sum();
//
//        int completedSessionsToday = (int) todaySessions.stream()
//                .filter(s -> s.getStatus() == FocusSessionStatus.COMPLETED)
//                .count();
//
//        // ---- Task counts ----
//        long pendingTasks = taskRepository.countByUserAndCompleted(user, false);
//        long completedTasks = taskRepository.countByUserAndCompleted(user, true);
//
//        // ---- Weekly focus chart (last 7 days, including today) ----
//        Map<String, Integer> weeklyFocusMinutes = new LinkedHashMap<>();
//
//        for (int i = 6; i >= 0; i--) {
//            LocalDate date = LocalDate.now().minusDays(i);
//            LocalDateTime dayStart = date.atStartOfDay();
//            LocalDateTime dayEnd = date.atTime(23, 59, 59);
//
//            List<FocusSession> daySessions = focusSessionRepository
//                    .findByUserIdAndStartedAtBetweenAndStatus(
//                            user.getId(), dayStart, dayEnd, FocusSessionStatus.COMPLETED);
//
//            int minutes = daySessions.stream()
//                    .mapToInt(FocusSession::getDuration)
//                    .sum();
//
//            String dayLabel = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
//            weeklyFocusMinutes.put(dayLabel, minutes);
//        }
//
//        // ---- Recent sessions (latest 5) ----
//        // Only show sessions whose task still exists — a session left behind
//        // after its task was deleted has nothing meaningful to display.
//        List<FocusSessionResponse> recentSessions = focusSessionRepository
//                .findTop5ByUserIdOrderByStartedAtDesc(user.getId())
//                .stream()
//                .filter(s -> s.getTask() != null)
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//
//        return new DashboardResponse(
//                totalFocusMinutesToday,
//                completedSessionsToday,
//                pendingTasks,
//                completedTasks,
//                weeklyFocusMinutes,
//                recentSessions
//        );
//    }
//
//    private FocusSessionResponse toResponse(FocusSession session) {
//        return new FocusSessionResponse(
//                session.getId(),
//                session.getTask() != null ? session.getTask().getId() : null,
//                session.getTask() != null ? session.getTask().getTitle() : null,
//                session.getDuration(),
//                session.getStartedAt(),
//                session.getCompletedAt(),
//                session.getCancelledAt(),
//                session.getStatus()
//        );
//    }
//}

package com.example.taskmanager.service;

import com.example.taskmanager.dto.DashboardResponse;
import com.example.taskmanager.dto.FocusSessionResponse;
import com.example.taskmanager.model.FocusSession;
import com.example.taskmanager.model.FocusSessionStatus;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.FocusSessionRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final FocusSessionRepository focusSessionRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final FocusSessionService focusSessionService;

    public DashboardService(FocusSessionRepository focusSessionRepository,
                            TaskRepository taskRepository,
                            UserRepository userRepository,
                            FocusSessionService focusSessionService) {
        this.focusSessionRepository = focusSessionRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.focusSessionService = focusSessionService;
    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    public DashboardResponse getDashboard() {
        User user = getAuthenticatedUser();

        // Auto-complete any RUNNING session whose scheduled time already
        // passed (e.g. left over from a browser tab that was closed
        // mid-session on a previous day) before computing anything below —
        // otherwise it would sit as "RUNNING" forever with no way to
        // resolve it from the UI.
        focusSessionService.resolveStaleRunningSessions(user.getId());

        // ---- Today's focus stats ----
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

        List<FocusSession> todaySessions = focusSessionRepository
                .findByUserIdAndStartedAtBetween(user.getId(), startOfDay, endOfDay);

        int totalFocusMinutesToday = todaySessions.stream()
                .filter(s -> s.getStatus() == FocusSessionStatus.COMPLETED)
                .mapToInt(FocusSession::getDuration)
                .sum();

        int completedSessionsToday = (int) todaySessions.stream()
                .filter(s -> s.getStatus() == FocusSessionStatus.COMPLETED)
                .count();

        // ---- Task counts ----
        long pendingTasks = taskRepository.countByUserAndCompleted(user, false);
        long completedTasks = taskRepository.countByUserAndCompleted(user, true);

        // ---- Weekly focus chart (last 7 days, including today) ----
        Map<String, Integer> weeklyFocusMinutes = new LinkedHashMap<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.atTime(23, 59, 59);

            List<FocusSession> daySessions = focusSessionRepository
                    .findByUserIdAndStartedAtBetweenAndStatus(
                            user.getId(), dayStart, dayEnd, FocusSessionStatus.COMPLETED);

            int minutes = daySessions.stream()
                    .mapToInt(FocusSession::getDuration)
                    .sum();

            String dayLabel = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            weeklyFocusMinutes.put(dayLabel, minutes);
        }

        // ---- Recent sessions (latest 5) ----
        // Only show sessions whose task still exists — a session left behind
        // after its task was deleted has nothing meaningful to display.
        List<FocusSessionResponse> recentSessions = focusSessionRepository
                .findTop5ByUserIdOrderByStartedAtDesc(user.getId())
                .stream()
                .filter(s -> s.getTask() != null)
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new DashboardResponse(
                totalFocusMinutesToday,
                completedSessionsToday,
                pendingTasks,
                completedTasks,
                weeklyFocusMinutes,
                recentSessions
        );
    }

    private FocusSessionResponse toResponse(FocusSession session) {
        return new FocusSessionResponse(
                session.getId(),
                session.getTask() != null ? session.getTask().getId() : null,
                session.getTask() != null ? session.getTask().getTitle() : null,
                session.getDuration(),
                session.getStartedAt(),
                session.getCompletedAt(),
                session.getCancelledAt(),
                session.getStatus()
        );
    }
}