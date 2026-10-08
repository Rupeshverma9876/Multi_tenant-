package multi_tenant.Security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import multi_tenant.Repository.UserRepository;
import multi_tenant.entity.User;

@Component
public class jwtAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepository userRepository;

    public jwtAuthFilter(
            JWTService jwtService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token =
                authHeader.substring(7);

        try {

            String email =
                    jwtService.extractEmail(token);

            Long tokenTenantId =
                    jwtService.extractTenantId(token);

            if (email != null &&
                    SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

                User user =
                        userRepository
                            .findByEmail(email)
                            .orElseThrow(() ->
                                new RuntimeException(
                                    "User not found"
                                ));

                Long databaseTenantId =
                        user.getTenant().getId();

                /*
                 * IMPORTANT:
                 * JWT tenant must match
                 * database tenant.
                 */

                if (!tokenTenantId.equals(
                        databaseTenantId)) {

                    response.setStatus(
                        HttpServletResponse
                            .SC_UNAUTHORIZED
                    );

                    response.getWriter().write(
                        "Invalid tenant"
                    );

                    return;
                }

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                            "ROLE_" +
                            user.getRole().name()
                        );

                UsernamePasswordAuthenticationToken
                        authentication =
                        new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            List.of(authority)
                        );

                authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                        .buildDetails(request)
                );

                SecurityContextHolder
                    .getContext()
                    .setAuthentication(
                        authentication
                    );

                TenantContext.setTenantId(
                    tokenTenantId
                );
            }

            filterChain.doFilter(
                    request,
                    response
            );

        } catch (Exception e) {

            response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
            );

            response.getWriter().write(
                "Invalid JWT"
            );

        } finally {

            TenantContext.clear();
        }
    }
}