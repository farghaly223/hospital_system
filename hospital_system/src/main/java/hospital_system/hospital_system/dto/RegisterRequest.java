package hospital_system.hospital_system.dto;

public class RegisterRequest {
    private String username;
    private String password;
    private String role;
    private String name;
    private String phone;
    private String specialization; 
    private String gender;

    public RegisterRequest() {}

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}