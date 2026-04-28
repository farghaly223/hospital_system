package hospital_system.hospital_system.dto;

public class UserStatusRequest {
    private Long userId;
    private boolean isActive;

    public UserStatusRequest() {}

    public UserStatusRequest(Long userId, boolean isActive) {
        this.userId = userId;
        this.isActive = isActive;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
