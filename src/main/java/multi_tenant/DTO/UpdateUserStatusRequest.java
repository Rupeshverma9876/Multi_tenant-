package multi_tenant.DTO;

public class UpdateUserStatusRequest {

    private boolean active;

    public UpdateUserStatusRequest() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}