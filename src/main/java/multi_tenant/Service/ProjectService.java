package multi_tenant.Service;

import lombok.RequiredArgsConstructor;
import multi_tenant.DTO.CreateProjectRequest;
import multi_tenant.Enum.ProjectStatus;
import multi_tenant.Repository.ProjectRepository;
import multi_tenant.Repository.TenantRepository;
import multi_tenant.Repository.UserRepository;
import multi_tenant.Security.TenantContext;
import multi_tenant.entity.Project;
import multi_tenant.entity.Tenant;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            TenantRepository tenantRepository,
            UserRepository userRepository) {

        this.projectRepository = projectRepository;
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
    }

    // CREATE PROJECT
    public Project createProject(CreateProjectRequest request) {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new RuntimeException("Tenant not found");
        }

        Tenant tenant = tenantRepository
                .findById(tenantId)
                .orElseThrow(() ->
                        new RuntimeException("Tenant not found"));

        Project project = new Project();

        project.setName(request.getName());

        project.setDescription(
                request.getDescription()
        );

        project.setTenant(tenant);

        // Stage 6
        project.setStatus(ProjectStatus.PLANNING);

        project.setCreatedAt(
                LocalDateTime.now()
        );

        return projectRepository.save(project);
    }

    // GET ALL PROJECTS OF CURRENT TENANT
    public List<Project> getProjects() {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new RuntimeException("Tenant not found");
        }

        return projectRepository
                .findByTenantId(tenantId);
    }

    // GET SINGLE PROJECT
    public Project getProject(Long projectId) {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new RuntimeException("Tenant not found");
        }

        return projectRepository
                .findByIdAndTenantId(
                        projectId,
                        tenantId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Project not found"
                        ));
    }

    // DELETE PROJECT
    public void deleteProject(Long projectId) {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new RuntimeException("Tenant not found");
        }

        Project project = projectRepository
                .findByIdAndTenantId(
                        projectId,
                        tenantId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Project not found"
                        ));

        projectRepository.delete(project);
    }
}