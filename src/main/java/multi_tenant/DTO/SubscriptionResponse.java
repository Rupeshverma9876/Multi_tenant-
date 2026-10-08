package multi_tenant.DTO;

public class SubscriptionResponse {
	private Long subscriptionId;
    private Long tenantId;

    private String plan;

    private int maxUsers;
    private int maxProjects;
    private int maxCustomers;

    private double price;

    private boolean active;

    public SubscriptionResponse(
            Long subscriptionId,
            Long tenantId,
            String plan,
            int maxUsers,
            int maxProjects,
            int maxCustomers,
            double price,
            boolean active) {

        this.subscriptionId = subscriptionId;
        this.tenantId = tenantId;
        this.plan = plan;
        this.maxUsers = maxUsers;
        this.maxProjects = maxProjects;
        this.maxCustomers = maxCustomers;
        this.price = price;
        this.active = active;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String getPlan() {
        return plan;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public int getMaxProjects() {
        return maxProjects;
    }

    public int getMaxCustomers() {
        return maxCustomers;
    }

    public double getPrice() {
        return price;
    }

    public boolean isActive() {
        return active;
    }
}
