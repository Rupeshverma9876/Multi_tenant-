package multi_tenant.DTO;



public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean active;
    private Long tenantId;

    public UserResponse(Long id,
                        String name,
                        String email,
                        String role,
                        boolean active,
                        Long tenantId) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
        this.tenantId = tenantId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public Long getTenantId() {
        return tenantId;
    }
}