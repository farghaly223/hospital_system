package hospital_system.hospital_system.dto;

public class ValidateResponse {
    private boolean valid;
    private String role;
    private String username;

    public ValidateResponse() {}
    public ValidateResponse(boolean valid, String role, String username) {
        this.valid = valid;
        this.role = role;
        this.username = username;
    }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}