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
import lombok.RequiredArgsConstructor;
import multi_tenant.DTO.CreateProjectRequest;
import multi_tenant.Service.ProjectService;
import multi_tenant.entity.Project;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(
            ProjectService projectService) {

        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(
            @Valid @RequestBody
            CreateProjectRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        projectService.createProject(
                                request
                        )
                );
    }

    @GetMapping
    public ResponseEntity<List<Project>> getProjects() {

        return ResponseEntity.ok(
                projectService.getProjects()
        );
    }
}
