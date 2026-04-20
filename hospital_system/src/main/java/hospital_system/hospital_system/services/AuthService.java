package hospital_system.hospital_system.services;

import hospital_system.hospital_system.dto.*;
import hospital_system.hospital_system.entities.User;
import hospital_system.hospital_system.repositories.UserRepository;
import hospital_system.hospital_system.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);
        return "Registered successfully";
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Incorrect username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect username or password");
        }

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