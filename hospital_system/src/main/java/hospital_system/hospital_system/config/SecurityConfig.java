package hospital_system.hospital_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
        System.out.println("DEBUG: SecurityConfig initialized with JwtAuthenticationFilter");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("DEBUG: Configuring SecurityFilterChain");

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/admin", "/api/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/api/doctor", "/api/doctor/**").hasAuthority("DOCTOR")
                .requestMatchers("/api/patient", "/api/patient/**").hasAuthority("PATIENT")
                
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("DEBUG: SecurityFilterChain configured successfully");
        System.out.println("DEBUG: Auth rules:");
        System.out.println("  - /auth/** -> permitAll");
        System.out.println("  - /api/admin/** -> hasAuthority('ADMIN')");
        System.out.println("  - /api/doctor/** -> hasAuthority('DOCTOR')");
        System.out.println("  - /api/patient/** -> hasAuthority('PATIENT')");
        System.out.println("  - anyRequest -> authenticated");

        return http.build();
    }
}
