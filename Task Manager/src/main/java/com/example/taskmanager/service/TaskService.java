//package com.example.taskmanager.service;
//
//import com.example.taskmanager.model.FocusSession;
//import com.example.taskmanager.model.Task;
//import com.example.taskmanager.model.User;
//import com.example.taskmanager.repository.FocusSessionRepository;
//import com.example.taskmanager.repository.TaskRepository;
//import com.example.taskmanager.repository.UserRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TaskService {
//
//    private final TaskRepository taskRepository;
//    private final UserRepository userRepository;
//    private final FocusSessionRepository focusSessionRepository;
//
//    public TaskService(TaskRepository taskRepository,
//                       UserRepository userRepository,
//                       FocusSessionRepository focusSessionRepository) {
//        this.taskRepository = taskRepository;
//        this.userRepository = userRepository;
//        this.focusSessionRepository = focusSessionRepository;
//    }
//
//    public List<Task> getTasksByUser(String username) {
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return taskRepository.findByUser(user);
//    }
//
//    public Task addTask(Task task, String username) {
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        task.setUser(user);
//
//        return taskRepository.save(task);
//    }
//
//    public void deleteTask(Long id, String username) {
//
//        Task task = taskRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Task not found"));
//
//        if (!task.getUser().getUsername().equals(username)) {
//            throw new RuntimeException("Unauthorized");
//        }
//
//        // Agar is task ke focus sessions hain, delete mat hone do
//        List<FocusSession> sessions = focusSessionRepository.findByTaskId(id);
//        if (!sessions.isEmpty()) {
//            throw new IllegalStateException(
//                    "Cannot delete task with existing focus sessions. Please delete the sessions first."
//            );
//        }
//
//        taskRepository.delete(task);
//    }
//
//    public List<Task> searchTasks(String keyword, String username) {
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        List<Task> tasks = taskRepository
//                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
//
//        return tasks.stream()
//                .filter(task -> task.getUser().getId().equals(user.getId()))
//                .toList();
//    }
//
//    public Task updateTask(Long id, Task updatedTask, String username) {
//
//        Task existingTask = taskRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Task not found"));
//
//        // 🔐 check owner
//        if (!existingTask.getUser().getUsername().equals(username)) {
//            throw new RuntimeException("Unauthorized");
//        }
//
//        // ✏️ update fields
//        existingTask.setTitle(updatedTask.getTitle());
//        existingTask.setDescription(updatedTask.getDescription());
//        existingTask.setDate(updatedTask.getDate());
//        existingTask.setCompleted(updatedTask.isCompleted());
//
//        return taskRepository.save(existingTask);
//    }
//}


package com.example.taskmanager.service;

import com.example.taskmanager.model.FocusSession;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.FocusSessionRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final FocusSessionRepository focusSessionRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       FocusSessionRepository focusSessionRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.focusSessionRepository = focusSessionRepository;
    }

    public List<Task> getTasksByUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByUser(user);
    }

    public Task addTask(Task task, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setUser(user);

        return taskRepository.save(task);
    }

    public void deleteTask(Long id, String username) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }

        // Delete this task's focus sessions first (its history), then the
        // task itself — deleting a task should just work, without forcing
        // the user to clean up its sessions manually first.
        List<FocusSession> sessions = focusSessionRepository.findByTaskId(id);
        if (!sessions.isEmpty()) {
            focusSessionRepository.deleteAll(sessions);
        }

        taskRepository.delete(task);
    }

    public List<Task> searchTasks(String keyword, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Task> tasks = taskRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);

        return tasks.stream()
                .filter(task -> task.getUser().getId().equals(user.getId()))
                .toList();
    }

    public Task updateTask(Long id, Task updatedTask, String username) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // 🔐 check owner
        if (!existingTask.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }

        // ✏️ update fields
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDate(updatedTask.getDate());
        existingTask.setCompleted(updatedTask.isCompleted());
        existingTask.setCategory(updatedTask.getCategory());
        existingTask.setPriority(updatedTask.getPriority());

        return taskRepository.save(existingTask);
    }
}