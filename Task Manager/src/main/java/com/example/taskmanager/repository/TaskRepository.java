package com.example.taskmanager.repository;

import com.example.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.taskmanager.model.Task;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);

    List<Task> findByTitleContainingIgnoreCase(String keyword);

    List<Task> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String keyword1, String keyword2);

    List<Task> findByUser(User user);

    long countByUserAndCompleted(User user, boolean completed);
}