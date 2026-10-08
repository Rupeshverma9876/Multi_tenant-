package multi_tenant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import multi_tenant.Enum.Role;

@Entity
@Table(name = "users")
public class User {
	
	

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String name;

	    @Column(nullable = false, unique = true)
	    private String email;

	    @Column(nullable = false)
	    private String password;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private Role role;

	    @Column(nullable = false)
	    private boolean active;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "tenant_id", nullable = false)
	    private Tenant tenant;

	    public User() {
	    }

	    public Long getId() {
	        return id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public Role getRole() {
	        return role;
	    }

	    public void setRole(Role role) {
	        this.role = role;
	    }

	    public boolean isActive() {
	        return active;
	    }

	    public void setActive(boolean active) {
	        this.active = active;
	    }

	    public Tenant getTenant() {
	        return tenant;
	    }

	    public void setTenant(Tenant tenant) {
	        this.tenant = tenant;
	    }
	
}