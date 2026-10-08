package multi_tenant.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import multi_tenant.DTO.CreateTaskRequest;
import multi_tenant.DTO.TaskResponse;
import multi_tenant.DTO.UpdateTaskStatusRequest;
import multi_tenant.Enum.TaskPriority;
import multi_tenant.Repository.NotificationRepository;
import multi_tenant.Repository.ProjectRepository;
import multi_tenant.Repository.TaskRepository;
import multi_tenant.Repository.TenantRepository;
import multi_tenant.Repository.UserRepository;
import multi_tenant.Security.TenantContext;
import multi_tenant.entity.Project;
import multi_tenant.entity.Task;

import multi_tenant.entity.TaskStatus;
import multi_tenant.entity.Tenant;
import multi_tenant.entity.User;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public TaskService(
            TaskRepository taskRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository,NotificationService notificationRepository ) {

        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationRepository;
    }

  
    public TaskResponse createTask(
            CreateTaskRequest request) {

        Long tenantId = getTenantId();

      
        Project project = projectRepository
                .findByIdAndTenantId(
                        request.getProjectId(),
                        tenantId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Project not found in your tenant"
                        ));

        
        User assignedUser = null;

        if (request.getAssignedTo() != null) {

            assignedUser = userRepository
                    .findByIdAndTenantId(
                            request.getAssignedTo(),
                            tenantId
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Assigned user not found in your tenant"
                            ));
        }

        
        TaskPriority priority;

        try {

            priority = TaskPriority.valueOf(
                    request.getPriority().toUpperCase()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid priority. Use LOW, MEDIUM, HIGH or CRITICAL"
            );
        }

        
        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(priority);

        
        task.setStatus(TaskStatus.TODO);

        task.setDueDate(request.getDueDate());
        task.setCreatedAt(LocalDateTime.now());

        
        task.setTenant(project.getTenant());

        task.setProject(project);

        task.setAssignedTo(assignedUser);

        Task savedTask =
                taskRepository.save(task);
        if (assignedUser != null) {

        	notificationService.sendEmailNotification(
        	        assignedUser.getId(),

        	        multi_tenant.Enum.NotificationType.TASK_ASSIGNED,

        	        "New Task Assigned",

        	        "Hello " + assignedUser.getName()
        	                + ",\n\nYou have been assigned a new task."
        	                + "\n\nTask: "
        	                + savedTask.getTitle()
        	                + "\nPriority: "
        	                + savedTask.getPriority().name()
        	                + "\nDue Date: "
        	                + savedTask.getDueDate()
        	);
        }

        return convert(savedTask);
    }

   
    public List<TaskResponse> getAllTasks() {

        Long tenantId = getTenantId();

        return taskRepository
                .findByTenantId(tenantId)
                .stream()
                .map(this::convert)
                .toList();
    }

    
    public List<TaskResponse> getTasks() {

        Long tenantId = TenantContext.getTenantId();

        return taskRepository.findByTenantId(tenantId)
                .stream()
                .map(this::convert)
                .toList();
    }

    
    public TaskResponse updateStatus(
            Long taskId,
            UpdateTaskStatusRequest request) {

        Long tenantId = getTenantId();

        Task task = taskRepository
                .findByIdAndTenantId(
                        taskId,
                        tenantId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Task not found"
                        ));

        TaskStatus status;

        try {

            status = TaskStatus.valueOf(
                    request.getStatus().toUpperCase()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid status. Use TODO, IN_PROGRESS, REVIEW or COMPLETED"
            );
        }

        task.setStatus(status);

        return convert(
                taskRepository.save(task)
        );
    }

   
    public void deleteTask(Long taskId) {

        Long tenantId = getTenantId();

        Task task = taskRepository
                .findByIdAndTenantId(
                        taskId,
                        tenantId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Task not found"
                        ));

        taskRepository.delete(task);
    }

    
    private Long getTenantId() {

        Long tenantId =
                TenantContext.getTenantId();

        if (tenantId == null) {

            throw new RuntimeException(
                    "Tenant not found"
            );
        }

        return tenantId;
    }

   
    private TaskResponse convert(Task task) {

        Long assignedUserId = null;

        if (task.getAssignedTo() != null) {

            assignedUserId =
                    task.getAssignedTo().getId();
        }

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getPriority().name(),
                task.getDueDate(),
                task.getProject().getId(),
                assignedUserId,
                task.getTenant().getId()
        );
    }
}