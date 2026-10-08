package multi_tenant.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterTenantRequest {
	 @NotBlank
	    private String companyName;

	    @NotBlank
	    private String subdomain;

	    @NotBlank
	    private String adminName;

	    @Email
	    @NotBlank
	    private String adminEmail;

	    @NotBlank
	    private String adminPassword;

	    public String getCompanyName() {
	        return companyName;
	    }

	    public void setCompanyName(String companyName) {
	        this.companyName = companyName;
	    }

	    public String getSubdomain() {
	        return subdomain;
	    }

	    public void setSubdomain(String subdomain) {
	        this.subdomain = subdomain;
	    }

	    public String getAdminName() {
	        return adminName;
	    }

	    public void setAdminName(String adminName) {
	        this.adminName = adminName;
	    }

	    public String getAdminEmail() {
	        return adminEmail;
	    }

	    public void setAdminEmail(String adminEmail) {
	        this.adminEmail = adminEmail;
	    }

	    public String getAdminPassword() {
	        return adminPassword;
	    }

	    public void setAdminPassword(String adminPassword) {
	        this.adminPassword = adminPassword;
	    }
}
