////package com.example.taskmanager.service;
////
////import com.example.taskmanager.dto.FocusSessionRequest;
////import com.example.taskmanager.dto.FocusSessionResponse;
////import com.example.taskmanager.model.FocusSession;
////import com.example.taskmanager.model.FocusSessionStatus;
////import com.example.taskmanager.model.Task;
////import com.example.taskmanager.model.User;
////import com.example.taskmanager.repository.FocusSessionRepository;
////import com.example.taskmanager.repository.TaskRepository;
////import com.example.taskmanager.repository.UserRepository;
////import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.stereotype.Service;
////
////import java.time.LocalDate;
////import java.time.LocalDateTime;
////import java.util.List;
////import java.util.stream.Collectors;
////
////@Service
////public class FocusSessionService {
////
////    private final FocusSessionRepository focusSessionRepository;
////    private final TaskRepository taskRepository;
////    private final UserRepository userRepository;
////
////    public FocusSessionService(FocusSessionRepository focusSessionRepository,
////                               TaskRepository taskRepository,
////                               UserRepository userRepository) {
////        this.focusSessionRepository = focusSessionRepository;
////        this.taskRepository = taskRepository;
////        this.userRepository = userRepository;
////    }
////
////    // ---- Helper: get the currently authenticated user ----
////    private User getAuthenticatedUser() {
////        String username = SecurityContextHolder.getContext().getAuthentication().getName();
////        return userRepository.findByUsername(username)
////                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
////    }
////
////    // ---- START SESSION ----
////    public FocusSessionResponse startSession(FocusSessionRequest request) {
////        User user = getAuthenticatedUser();
////
////        Task task = taskRepository.findById(request.getTaskId())
////                .orElseThrow(() -> new RuntimeException("Task not found"));
////
////        if (!task.getUser().getId().equals(user.getId())) {
////            throw new SecurityException("You do not have access to this task");
////        }
////
////        if (request.getDuration() == null || request.getDuration() <= 0) {
////            throw new IllegalArgumentException("Duration must be greater than 0");
////        }
////
////        FocusSession session = new FocusSession();
////        session.setUser(user);
////        session.setTask(task);
////        session.setDuration(request.getDuration());
////        session.setStartedAt(LocalDateTime.now());
////        session.setStatus(FocusSessionStatus.RUNNING);
////
////        FocusSession saved = focusSessionRepository.save(session);
////        return toResponse(saved);
////    }
////
////    // ---- COMPLETE SESSION ----
////    public FocusSessionResponse completeSession(Long sessionId) {
////        User user = getAuthenticatedUser();
////
////        FocusSession session = focusSessionRepository.findById(sessionId)
////                .orElseThrow(() -> new RuntimeException("Focus session not found"));
////
////        if (!session.getUser().getId().equals(user.getId())) {
////            throw new SecurityException("You do not have access to this session");
////        }
////
////        if (session.getStatus() == FocusSessionStatus.COMPLETED) {
////            throw new IllegalStateException("Session is already completed");
////        }
////
////        if (session.getStatus() == FocusSessionStatus.CANCELLED) {
////            throw new IllegalStateException("Cannot complete a cancelled session");
////        }
////
////        session.setStatus(FocusSessionStatus.COMPLETED);
////        session.setCompletedAt(LocalDateTime.now());
////
////        FocusSession saved = focusSessionRepository.save(session);
////        return toResponse(saved);
////    }
////
////    // ---- CANCEL SESSION ----
////    public FocusSessionResponse cancelSession(Long sessionId) {
////        User user = getAuthenticatedUser();
////
////        FocusSession session = focusSessionRepository.findById(sessionId)
////                .orElseThrow(() -> new RuntimeException("Focus session not found"));
////
////        if (!session.getUser().getId().equals(user.getId())) {
////            throw new SecurityException("You do not have access to this session");
////        }
////
////        if (session.getStatus() == FocusSessionStatus.COMPLETED) {
////            throw new IllegalStateException("Cannot cancel a completed session");
////        }
////
////        session.setStatus(FocusSessionStatus.CANCELLED);
////
////        FocusSession saved = focusSessionRepository.save(session);
////        return toResponse(saved);
////    }
////
////    // ---- DELETE SESSION ----
////    public void deleteSession(Long sessionId) {
////        User user = getAuthenticatedUser();
////
////        FocusSession session = focusSessionRepository.findById(sessionId)
////                .orElseThrow(() -> new RuntimeException("Focus session not found"));
////
////        if (!session.getUser().getId().equals(user.getId())) {
////            throw new SecurityException("You do not have access to this session");
////        }
////
////        focusSessionRepository.delete(session);
////    }
////
////    // ---- GET ALL SESSIONS OF LOGGED-IN USER ----
////    public List<FocusSessionResponse> getUserSessions() {
////        User user = getAuthenticatedUser();
////
////        return focusSessionRepository.findByUserId(user.getId())
////                .stream()
////                .map(this::toResponse)
////                .collect(Collectors.toList());
////    }
////
////    // ---- GET TODAY'S SESSIONS ----
////    public List<FocusSessionResponse> getTodaySessions() {
////        User user = getAuthenticatedUser();
////
////        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
////        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
////
////        return focusSessionRepository
////                .findByUserIdAndStartedAtBetween(user.getId(), startOfDay, endOfDay)
////                .stream()
////                .map(this::toResponse)
////                .collect(Collectors.toList());
////    }
////
////    // ---- GET SESSIONS FOR A SPECIFIC TASK ----
////    public List<FocusSessionResponse> getTaskSessions(Long taskId) {
////        User user = getAuthenticatedUser();
////
////        Task task = taskRepository.findById(taskId)
////                .orElseThrow(() -> new RuntimeException("Task not found"));
////
////        if (!task.getUser().getId().equals(user.getId())) {
////            throw new SecurityException("You do not have access to this task");
////        }
////
////        return focusSessionRepository.findByTaskId(taskId)
////                .stream()
////                .map(this::toResponse)
////                .collect(Collectors.toList());
////
////
////    }
////
////    // ---- Helper: convert Entity -> DTO ----
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
////
////
////}
//
//
//package com.example.taskmanager.service;
//
//import com.example.taskmanager.dto.FocusSessionRequest;
//import com.example.taskmanager.dto.FocusSessionResponse;
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
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class FocusSessionService {
//
//    private final FocusSessionRepository focusSessionRepository;
//    private final TaskRepository taskRepository;
//    private final UserRepository userRepository;
//
//    public FocusSessionService(FocusSessionRepository focusSessionRepository,
//                               TaskRepository taskRepository,
//                               UserRepository userRepository) {
//        this.focusSessionRepository = focusSessionRepository;
//        this.taskRepository = taskRepository;
//        this.userRepository = userRepository;
//    }
//
//    // ---- Helper: get the currently authenticated user ----
//    private User getAuthenticatedUser() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
//    }
//
//    // ---- START SESSION ----
//    public FocusSessionResponse startSession(FocusSessionRequest request) {
//        User user = getAuthenticatedUser();
//
//        Task task = taskRepository.findById(request.getTaskId())
//                .orElseThrow(() -> new RuntimeException("Task not found"));
//
//        if (!task.getUser().getId().equals(user.getId())) {
//            throw new SecurityException("You do not have access to this task");
//        }
//
//        if (request.getDuration() == null || request.getDuration() <= 0) {
//            throw new IllegalArgumentException("Duration must be greater than 0");
//        }
//
//        FocusSession session = new FocusSession();
//        session.setUser(user);
//        session.setTask(task);
//        session.setDuration(request.getDuration());
//        session.setStartedAt(LocalDateTime.now());
//        session.setStatus(FocusSessionStatus.RUNNING);
//
//        FocusSession saved = focusSessionRepository.save(session);
//        return toResponse(saved);
//    }
//
//    // ---- COMPLETE SESSION ----
//    public FocusSessionResponse completeSession(Long sessionId) {
//        User user = getAuthenticatedUser();
//
//        FocusSession session = focusSessionRepository.findById(sessionId)
//                .orElseThrow(() -> new RuntimeException("Focus session not found"));
//
//        if (!session.getUser().getId().equals(user.getId())) {
//            throw new SecurityException("You do not have access to this session");
//        }
//
//        if (session.getStatus() == FocusSessionStatus.COMPLETED) {
//            throw new IllegalStateException("Session is already completed");
//        }
//
//        if (session.getStatus() == FocusSessionStatus.CANCELLED) {
//            throw new IllegalStateException("Cannot complete a cancelled session");
//        }
//
//        session.setStatus(FocusSessionStatus.COMPLETED);
//        session.setCompletedAt(LocalDateTime.now());
//
//        FocusSession saved = focusSessionRepository.save(session);
//        return toResponse(saved);
//    }
//
//    // ---- CANCEL SESSION ----
//    public FocusSessionResponse cancelSession(Long sessionId) {
//        User user = getAuthenticatedUser();
//
//        FocusSession session = focusSessionRepository.findById(sessionId)
//                .orElseThrow(() -> new RuntimeException("Focus session not found"));
//
//        if (!session.getUser().getId().equals(user.getId())) {
//            throw new SecurityException("You do not have access to this session");
//        }
//
//        if (session.getStatus() == FocusSessionStatus.COMPLETED) {
//            throw new IllegalStateException("Cannot cancel a completed session");
//        }
//
//        session.setStatus(FocusSessionStatus.CANCELLED);
//        session.setCancelledAt(LocalDateTime.now());
//
//        FocusSession saved = focusSessionRepository.save(session);
//        return toResponse(saved);
//    }
//
//    // ---- DELETE SESSION ----
//    public void deleteSession(Long sessionId) {
//        User user = getAuthenticatedUser();
//
//        FocusSession session = focusSessionRepository.findById(sessionId)
//                .orElseThrow(() -> new RuntimeException("Focus session not found"));
//
//        if (!session.getUser().getId().equals(user.getId())) {
//            throw new SecurityException("You do not have access to this session");
//        }
//
//        focusSessionRepository.delete(session);
//    }
//
//    // ---- GET ALL SESSIONS OF LOGGED-IN USER ----
//    public List<FocusSessionResponse> getUserSessions() {
//        User user = getAuthenticatedUser();
//
//        return focusSessionRepository.findByUserId(user.getId())
//                .stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    // ---- GET TODAY'S SESSIONS ----
//    public List<FocusSessionResponse> getTodaySessions() {
//        User user = getAuthenticatedUser();
//
//        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
//        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
//
//        return focusSessionRepository
//                .findByUserIdAndStartedAtBetween(user.getId(), startOfDay, endOfDay)
//                .stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    // ---- GET SESSIONS FOR A SPECIFIC TASK ----
//    public List<FocusSessionResponse> getTaskSessions(Long taskId) {
//        User user = getAuthenticatedUser();
//
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new RuntimeException("Task not found"));
//
//        if (!task.getUser().getId().equals(user.getId())) {
//            throw new SecurityException("You do not have access to this task");
//        }
//
//        return focusSessionRepository.findByTaskId(taskId)
//                .stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//
//
//    }
//
//    // ---- Helper: convert Entity -> DTO ----
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
//
//
//}


package com.example.taskmanager.service;

import com.example.taskmanager.dto.FocusSessionRequest;
import com.example.taskmanager.dto.FocusSessionResponse;
import com.example.taskmanager.model.FocusSession;
import com.example.taskmanager.model.FocusSessionStatus;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.FocusSessionRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FocusSessionService {

    private final FocusSessionRepository focusSessionRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public FocusSessionService(FocusSessionRepository focusSessionRepository,
                               TaskRepository taskRepository,
                               UserRepository userRepository) {
        this.focusSessionRepository = focusSessionRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // ---- Helper: get the currently authenticated user ----
    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    // ---- Auto-resolve stale RUNNING sessions ----
    // A session left RUNNING (e.g. the browser was closed mid-session and
    // never came back to cancel/complete it) would otherwise stay RUNNING
    // forever. Any time we read a user's sessions, first check whether the
    // scheduled end time (startedAt + duration) has already passed, and if
    // so, mark it COMPLETED as of that scheduled end time.
    public void resolveStaleRunningSessions(Long userId) {
        List<FocusSession> runningSessions =
                focusSessionRepository.findByUserIdAndStatus(userId, FocusSessionStatus.RUNNING);

        LocalDateTime now = LocalDateTime.now();
        for (FocusSession session : runningSessions) {
            if (session.getStartedAt() == null) continue;
            LocalDateTime scheduledEnd = session.getStartedAt().plusMinutes(session.getDuration());
            if (now.isAfter(scheduledEnd)) {
                session.setStatus(FocusSessionStatus.COMPLETED);
                session.setCompletedAt(scheduledEnd);
                focusSessionRepository.save(session);
            }
        }
    }

    // ---- START SESSION ----
    public FocusSessionResponse startSession(FocusSessionRequest request) {
        User user = getAuthenticatedUser();

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You do not have access to this task");
        }

        if (request.getDuration() == null || request.getDuration() <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }

        FocusSession session = new FocusSession();
        session.setUser(user);
        session.setTask(task);
        session.setDuration(request.getDuration());
        session.setStartedAt(LocalDateTime.now());
        session.setStatus(FocusSessionStatus.RUNNING);

        FocusSession saved = focusSessionRepository.save(session);
        return toResponse(saved);
    }

    // ---- COMPLETE SESSION ----
    public FocusSessionResponse completeSession(Long sessionId) {
        User user = getAuthenticatedUser();

        FocusSession session = focusSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Focus session not found"));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You do not have access to this session");
        }

        if (session.getStatus() == FocusSessionStatus.COMPLETED) {
            throw new IllegalStateException("Session is already completed");
        }

        if (session.getStatus() == FocusSessionStatus.CANCELLED) {
            throw new IllegalStateException("Cannot complete a cancelled session");
        }

        session.setStatus(FocusSessionStatus.COMPLETED);
        session.setCompletedAt(LocalDateTime.now());

        FocusSession saved = focusSessionRepository.save(session);
        return toResponse(saved);
    }

    // ---- CANCEL SESSION ----
    public FocusSessionResponse cancelSession(Long sessionId) {
        User user = getAuthenticatedUser();

        FocusSession session = focusSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Focus session not found"));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You do not have access to this session");
        }

        if (session.getStatus() == FocusSessionStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed session");
        }

        session.setStatus(FocusSessionStatus.CANCELLED);
        session.setCancelledAt(LocalDateTime.now());

        FocusSession saved = focusSessionRepository.save(session);
        return toResponse(saved);
    }

    // ---- DELETE SESSION ----
    public void deleteSession(Long sessionId) {
        User user = getAuthenticatedUser();

        FocusSession session = focusSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Focus session not found"));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You do not have access to this session");
        }

        focusSessionRepository.delete(session);
    }

    // ---- GET ALL SESSIONS OF LOGGED-IN USER ----
    public List<FocusSessionResponse> getUserSessions() {
        User user = getAuthenticatedUser();
        resolveStaleRunningSessions(user.getId());

        return focusSessionRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ---- GET TODAY'S SESSIONS ----
    public List<FocusSessionResponse> getTodaySessions() {
        User user = getAuthenticatedUser();
        resolveStaleRunningSessions(user.getId());

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

        return focusSessionRepository
                .findByUserIdAndStartedAtBetween(user.getId(), startOfDay, endOfDay)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ---- GET SESSIONS FOR A SPECIFIC TASK ----
    public List<FocusSessionResponse> getTaskSessions(Long taskId) {
        User user = getAuthenticatedUser();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You do not have access to this task");
        }

        return focusSessionRepository.findByTaskId(taskId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());


    }

    // ---- Helper: convert Entity -> DTO ----
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