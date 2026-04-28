package hospital_system.hospital_system.controllers;

import hospital_system.hospital_system.dto.AdminRequest;
import hospital_system.hospital_system.dto.DoctorApprovalRequest;
import hospital_system.hospital_system.dto.UserStatusRequest;
import hospital_system.hospital_system.entities.Admin;
import hospital_system.hospital_system.entities.Doctor;
import hospital_system.hospital_system.entities.User;
import hospital_system.hospital_system.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(adminService.getAdminById(id));
        } catch (AdminService.ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody AdminRequest request) {
        try {
            Admin admin = adminService.createAdmin(request);
            admin.getUser().setPassword(null);
            return new ResponseEntity<>(admin, HttpStatus.CREATED);
        } catch (AdminService.ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        try {
            Admin updated = adminService.updateAdmin(id, admin);
            return ResponseEntity.ok(updated);
        } catch (AdminService.ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.ok("Admin deleted successfully");
        } catch (AdminService.ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/approve-doctor")
    public ResponseEntity<?> approveDoctorAndSetFee(@RequestBody DoctorApprovalRequest request) {
        try {
            adminService.approveDoctorAndSetFee(request);
            return ResponseEntity.ok(request.isApproved() ? "Doctor approved successfully" : "Doctor approval revoked");
        } catch (AdminService.ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/update-user-status")
    public ResponseEntity<?> updateUserStatus(@RequestBody UserStatusRequest request) {
        try {
            adminService.updateUserStatus(request);
            return ResponseEntity.ok(request.isActive() ? "User activated successfully" : "User deactivated successfully");
        } catch (AdminService.ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/pending-doctors")
    public ResponseEntity<List<Doctor>> getPendingDoctors() {
        return ResponseEntity.ok(adminService.getPendingDoctors());
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(adminService.getAllDoctors());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            adminService.deleteAdmin(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (AdminService.ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
