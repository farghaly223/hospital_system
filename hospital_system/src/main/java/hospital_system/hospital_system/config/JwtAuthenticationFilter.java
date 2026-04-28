package hospital_system.hospital_system.config;

import hospital_system.hospital_system.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        System.out.println("\n=== JWT FILTER DEBUG ===");
        System.out.println("Request Path: " + requestPath);
        System.out.println("Request Method: " + request.getMethod());

        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header Present: " + (authHeader != null));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No valid Bearer token found, skipping JWT processing");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username = jwtUtil.extractUsername(jwt);
        System.out.println("Extracted Username: " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Processing JWT for user: " + username);

            if (jwtUtil.validateToken(jwt)) {
                String role = jwtUtil.extractRole(jwt);
                System.out.println("Token Valid - Role from JWT: " + role);

                List<GrantedAuthority> authorities = new ArrayList<>();

                if (role != null && !role.isEmpty()) {
                    String normalizedRole = role.toUpperCase().replace("ROLE_", "");
                    authorities.add(new SimpleGrantedAuthority(normalizedRole));
                    System.out.println("Added Authority: " + normalizedRole);
                } else {
                    System.out.println("WARNING: Role is null or empty!");
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, authorities
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("DEBUG: Auth success for: " + username + " | Role from token: " + role + " | Authorities set: " + authorities);
                System.out.println("SecurityContext Authentication: " + SecurityContextHolder.getContext().getAuthentication());
                System.out.println("Authorities in SecurityContext: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            } else {
                System.out.println("Token validation failed!");
            }
        } else {
            if (username == null) {
                System.out.println("Username is null");
            }
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                System.out.println("Authentication already exists in SecurityContext");
            }
        }

        System.out.println("=== END JWT FILTER DEBUG ===\n");
        filterChain.doFilter(request, response);
    }
}
