package hospital_system.hospital_system.services;

import hospital_system.hospital_system.dto.AdminRequest;
import hospital_system.hospital_system.dto.DoctorApprovalRequest;
import hospital_system.hospital_system.dto.UserStatusRequest;
import hospital_system.hospital_system.entities.Admin;
import hospital_system.hospital_system.entities.Doctor;
import hospital_system.hospital_system.entities.User;
import hospital_system.hospital_system.repositories.AdminRepository;
import hospital_system.hospital_system.repositories.DoctorRepository;
import hospital_system.hospital_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));
    }

    @Transactional
    public Admin createAdmin(AdminRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceNotFoundException("Username already exists!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ADMIN");
        user.setIsActive(true);

        User savedUser = userRepository.saveAndFlush(user);

        Admin admin = new Admin();
        admin.setUser(savedUser);
        admin.setPermissions("DEFAULT");
        admin.setCreatedAt(System.currentTimeMillis());
        return adminRepository.save(admin);
    }

    @Transactional
    public Admin updateAdmin(Long id, Admin updatedAdmin) {
        Admin existingAdmin = getAdminById(id);
        if (updatedAdmin.getPermissions() != null) {
            existingAdmin.setPermissions(updatedAdmin.getPermissions());
        }
        return adminRepository.save(existingAdmin);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        Admin admin = getAdminById(id);
        User user = admin.getUser();
        adminRepository.deleteById(id);
        if (user != null) {
            userRepository.deleteById(user.getId());
        }
    }

    @Transactional
    public void updateUserStatus(UserStatusRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
        user.setIsActive(request.isActive());
        userRepository.save(user);
    }

    @Transactional
    public void approveDoctorAndSetFee(DoctorApprovalRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));
        doctor.setApproved(request.isApproved());
        if (request.getConsultationFee() != null) {
            doctor.setConsultationFee(request.getConsultationFee());
        }
        doctorRepository.save(doctor);
    }

    public List<Doctor> getPendingDoctors() {
        return doctorRepository.findAll().stream()
                .filter(d -> !d.isApproved())
                .toList();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
