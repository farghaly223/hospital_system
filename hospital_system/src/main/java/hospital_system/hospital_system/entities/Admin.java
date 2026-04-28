package hospital_system.hospital_system.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "permissions", columnDefinition = "TEXT")
    private String permissions;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt = System.currentTimeMillis();

    public Admin() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
}
