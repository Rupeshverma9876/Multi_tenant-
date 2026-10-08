package multi_tenant.DTO;

import java.time.LocalDateTime;

public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private Long projectId;
    private Long assignedTo;
    private Long tenantId;

    public TaskResponse(
            Long id,
            String title,
            String description,
            String status,
            String priority,
            LocalDateTime dueDate,
            Long projectId,
            Long assignedTo,
            Long tenantId) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.projectId = projectId;
        this.assignedTo = assignedTo;
        this.tenantId = tenantId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public Long getTenantId() {
        return tenantId;
    }
}