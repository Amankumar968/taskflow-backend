package com.example.taskmanager.controller;


import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {



    private final TaskService taskService;



    public TaskController(TaskService taskService){

        this.taskService = taskService;

    }




    // GET ALL USER TASKS

    @GetMapping
    public List<Task> getTask(Authentication authentication){


        String username =
                authentication.getName();


        return taskService.getTasksByUser(username);

    }





    // CREATE TASK

    @PostMapping
    public Task createTask(
            @RequestBody Task task,
            Authentication authentication){


        String username =
                authentication.getName();


        return taskService.addTask(task, username);

    }





    // DELETE TASK

    @DeleteMapping("/{id}")
    public String deleteTask(
            @PathVariable Long id,
            Authentication authentication){


        String username =
                authentication.getName();


        taskService.deleteTask(id, username);


        return "Deleted";

    }

// UPDATE TASK

    @PutMapping("/{id}")
    public Task updateTask(
            @PathVariable Long id,
            @RequestBody Task task,
            Authentication authentication
    ){

        String username = authentication.getName();

        return taskService.updateTask(
                id,
                task,
                username
        );
    }



    // SEARCH TASK

    @GetMapping("/search")
    public List<Task> searchTasks(
            @RequestParam String keyword,
            Authentication authentication){



        String username =
                authentication.getName();



        return taskService.searchTasks(
                keyword,
                username
        );

    }


}