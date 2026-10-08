package multi_tenant.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import multi_tenant.DTO.CreateTaskRequest;
import multi_tenant.DTO.TaskResponse;
import multi_tenant.Service.TaskService;
import multi_tenant.entity.Task;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @RequestBody CreateTaskRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(request));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks() {

        return ResponseEntity.ok(
            taskService.getTasks()
        );
    
    }
}
