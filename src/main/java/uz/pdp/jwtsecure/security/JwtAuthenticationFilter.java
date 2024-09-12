package uz.pdp.jwtsecure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.jwtsecure.entities.User;
import uz.pdp.jwtsecure.repository.UserRepository;
import uz.pdp.jwtsecure.services.AuthService;
import uz.pdp.jwtsecure.utils.RestConstants;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthService authService;

    private final UserRepository userRepository;

    public JwtAuthenticationFilter( JwtTokenProvider jwtTokenProvider,
                                   @Lazy AuthService authService, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            setUserPrincipalIfAllOk(httpServletRequest);
        } catch (Exception e) {
            System.out.println("Error");
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void setUserPrincipalIfAllOk(HttpServletRequest request) {
        String authorization = request.getHeader(RestConstants.AUTHENTICATION_HEADER);

        if (authorization != null) {
            UserPrincipal userPrincipal = getUserFromBearerToken(authorization);
            if (userPrincipal != null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }


    public UserPrincipal getUserFromBearerToken(String token) {

        try {
            token = token.trim();
            if (token.startsWith(RestConstants.BEARER_TOKEN)) {
                token = token.substring(RestConstants.BEARER_TOKEN.length()).trim();
                Jws<Claims> claimsJws;
                if ((claimsJws = jwtTokenProvider.getClaimsJws(token, Boolean.TRUE)) != null) {
                    String userId = jwtTokenProvider.getUserIdFromToken(claimsJws);
                    Optional<User> optionalUser = userRepository.findById(UUID.fromString(userId));
                    if (optionalUser.isPresent()) {
                        UserPrincipal userPrincipal = new UserPrincipal(optionalUser.get());
                        if (userPrincipal.allOk())
                            return userPrincipal;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
