package com.example.taskmanager.controller;

import com.example.taskmanager.dto.FocusSessionRequest;
import com.example.taskmanager.dto.FocusSessionResponse;
import com.example.taskmanager.service.FocusSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/focus-sessions")
public class FocusSessionController {

    private final FocusSessionService focusSessionService;

    public FocusSessionController(FocusSessionService focusSessionService) {
        this.focusSessionService = focusSessionService;
    }

    // POST /focus-sessions/start
    @PostMapping("/start")
    public ResponseEntity<?> startSession(@RequestBody FocusSessionRequest request) {
        try {
            FocusSessionResponse response = focusSessionService.startSession(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // PUT /focus-sessions/{id}/complete
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeSession(@PathVariable Long id) {
        try {
            FocusSessionResponse response = focusSessionService.completeSession(id);
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // PUT /focus-sessions/{id}/cancel
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelSession(@PathVariable Long id) {
        try {
            FocusSessionResponse response = focusSessionService.cancelSession(id);
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    // DELETE /focus-sessions/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable Long id) {
        try {
            focusSessionService.deleteSession(id);
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET /focus-sessions
    @GetMapping
    public ResponseEntity<List<FocusSessionResponse>> getUserSessions() {
        return ResponseEntity.ok(focusSessionService.getUserSessions());
    }

    // GET /focus-sessions/today
    @GetMapping("/today")
    public ResponseEntity<List<FocusSessionResponse>> getTodaySessions() {
        return ResponseEntity.ok(focusSessionService.getTodaySessions());
    }

    // GET /focus-sessions/task/{taskId}
    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getTaskSessions(@PathVariable Long taskId) {
        try {
            List<FocusSessionResponse> response = focusSessionService.getTaskSessions(taskId);
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}