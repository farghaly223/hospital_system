package hospital_system.hospital_system.services;

import hospital_system.hospital_system.dto.*;
import hospital_system.hospital_system.entities.*;
import hospital_system.hospital_system.repositories.*;
import hospital_system.hospital_system.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, 
                       DoctorRepository doctorRepository, 
                       PatientRepository patientRepository, 
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String register(RegisterRequest request) {
        // 1. التأكد من عدم تكرار اسم المستخدم
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }

        // 2. إنشاء اليوزر وتشفير الباسورد
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // توحيد حالة الـ Role لتجنب المشاكل في الـ Comparison
        String role = request.getRole().toUpperCase();
        user.setRole(role);

        // حفظ اليوزر فوراً للحصول على الـ ID (مهم جداً للـ Foreign Key)
        User savedUser = userRepository.saveAndFlush(user);

        // 3. إنشاء البروفايل المرتبط بناءً على الـ Role
        if ("DOCTOR".equals(role)) {
            Doctor doctor = new Doctor();
            doctor.setUser(savedUser); // الربط عبر الـ @OneToOne
            doctor.setName(request.getName());
            doctor.setSpecialization(request.getSpecialization());
            doctor.setPhone(request.getPhone());
            doctor.setApproved(false); // دايماً بيبدأ غير مفعل للأمان
            doctorRepository.save(doctor);
            
        } else if ("PATIENT".equals(role)) {
            Patient patient = new Patient();
            patient.setUser(savedUser); // الربط عبر الـ @OneToOne
            patient.setName(request.getName());
            patient.setPhone(request.getPhone());
            patient.setGender(request.getGender());
            // لاحظ: باقي الحقول (تاريخ الميلاد، إلخ) ممكن تضاف في تحديث الملف الشخصي لاحقاً
            patientRepository.save(patient);
        }

        return "Registered successfully as " + role;
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Incorrect username or password"));

        // التأكد من صحة الباسورد
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect username or password");
        }

        // توليد التوكن
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token, user.getRole(), user.getUsername());
    }

    public ValidateResponse validate(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        boolean isValid = jwtUtil.validateToken(token);
        if (!isValid) {
            return new ValidateResponse(false, null, null);
        }

        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);
        return new ValidateResponse(true, role, username);
    }
}