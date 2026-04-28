package hospital_system.hospital_system.services;

import hospital_system.hospital_system.dto.*;
import hospital_system.hospital_system.entities.*;
import hospital_system.hospital_system.repositories.*;
import hospital_system.hospital_system.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       DoctorRepository doctorRepository,
                       PatientRepository patientRepository,
                       AdminRepository adminRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.adminRepository = adminRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String register(RegisterRequest request) {
        System.out.println("DEBUG AUTH: Registering user: " + request.getUsername() + " with role: " + request.getRole());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        String role = request.getRole().toUpperCase();
        user.setRole(role);
        user.setIsActive(true);

        System.out.println("DEBUG AUTH: User role set to: " + role);

        User savedUser = userRepository.saveAndFlush(user);
        System.out.println("DEBUG AUTH: User saved with ID: " + savedUser.getId() + " and role: " + savedUser.getRole());

        if ("DOCTOR".equals(role)) {
            Doctor doctor = new Doctor();
            doctor.setUser(savedUser);
            doctor.setName(request.getName());
            doctor.setSpecialization(request.getSpecialization());
            doctor.setPhone(request.getPhone());
            doctor.setApproved(false);
            doctorRepository.save(doctor);
            System.out.println("DEBUG AUTH: Doctor profile created");

        } else if ("PATIENT".equals(role)) {
            Patient patient = new Patient();
            patient.setUser(savedUser);
            patient.setName(request.getName());
            patient.setPhone(request.getPhone());
            patient.setGender(request.getGender());
            patientRepository.save(patient);
            System.out.println("DEBUG AUTH: Patient profile created");

        } else if ("ADMIN".equals(role)) {
            Admin admin = new Admin();
            admin.setUser(savedUser);
            admin.setPermissions("DEFAULT");
            adminRepository.save(admin);
            System.out.println("DEBUG AUTH: Admin profile created");
        }

        return "Registered successfully as " + role;
    }

    public AuthResponse login(AuthRequest request) {
        System.out.println("DEBUG AUTH: Login attempt for user: " + request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Incorrect username or password"));

        System.out.println("DEBUG AUTH: User found. Role in DB: " + user.getRole());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect username or password");
        }

        System.out.println("DEBUG AUTH: Password matched");

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        System.out.println("DEBUG AUTH: Token generated for user: " + user.getUsername() + " with role: " + user.getRole());

        return new AuthResponse(token, user.getRole(), user.getUsername());
    }

    public ValidateResponse validate(String token) {
        System.out.println("DEBUG AUTH: Validating token");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        boolean isValid = jwtUtil.validateToken(token);
        System.out.println("DEBUG AUTH: Token valid: " + isValid);

        if (!isValid) {
            return new ValidateResponse(false, null, null);
        }

        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);
        System.out.println("DEBUG AUTH: Token contains - Username: " + username + ", Role: " + role);

        return new ValidateResponse(true, role, username);
    }
}
