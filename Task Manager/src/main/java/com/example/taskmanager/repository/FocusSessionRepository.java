package com.example.taskmanager.repository;

import com.example.taskmanager.model.FocusSession;
import com.example.taskmanager.model.FocusSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FocusSessionRepository extends JpaRepository<FocusSession, Long> {


    List<FocusSession> findByUserId(Long userId);


    List<FocusSession> findByUserIdAndStartedAtBetween(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );


    List<FocusSession> findByTaskId(Long taskId);




    List<FocusSession> findTop5ByUserIdOrderByStartedAtDesc(Long userId);


    List<FocusSession> findByUserIdAndStartedAtBetweenAndStatus(
            Long userId,
            LocalDateTime start,
            LocalDateTime end,
            FocusSessionStatus status
    );

    List<FocusSession> findByUserIdAndStatus(Long userId, FocusSessionStatus status);
}