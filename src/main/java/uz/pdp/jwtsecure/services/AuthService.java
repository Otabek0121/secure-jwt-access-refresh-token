package uz.pdp.jwtsecure.services;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.jwtsecure.entities.User;
import uz.pdp.jwtsecure.exceptions.RestException;
import uz.pdp.jwtsecure.dtos.*;
import uz.pdp.jwtsecure.repository.UserRepository;
import uz.pdp.jwtsecure.security.JwtTokenProvider;
import uz.pdp.jwtsecure.utils.MessageConstants;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static uz.pdp.jwtsecure.utils.MessageConstants.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    public User getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RestException(MessageConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        return user;
    }

    public ApiResult<TokenDTO> login(SignInRequestDTO signInRequestDTO) {

        User user = userRepository.findByUsername(signInRequestDTO.getUsername())
                .orElseThrow(() -> RestException.restThrow(MessageConstants.USER_NOT_FOUND));


        if (!passwordEncoder.matches(signInRequestDTO.getPassword(), user.getPassword()))
            throw RestException.restThrow(MessageConstants.LOGIN_OR_PASSWORD_ERROR);

        String accessToken = jwtTokenProvider.generateAccessToken(user, Timestamp.valueOf(LocalDateTime.now()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        return ApiResult.success(
                TokenDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build());
    }

    public ApiResult<TokenDTO> refreshToken(RefreshTokenDTO refreshTokenDTO) {


        String accessToken = refreshTokenDTO.getAccessToken().trim();
        accessToken = getTokenWithOutBearer(accessToken);
        try {
            jwtTokenProvider.getClaimsJws(accessToken, Boolean.TRUE);
        } catch (ExpiredJwtException ex) {
            try {
                String refreshToken = refreshTokenDTO.getRefreshToken();
                refreshToken = getTokenWithOutBearer(refreshToken);

                String userId = jwtTokenProvider.getUserIdFromToken(refreshToken, Boolean.FALSE);
                User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> RestException.restThrow(TOKEN_NOT_VALID));

                if (!user.isEnabled()
                        || !user.isAccountNonExpired()
                        || !user.isAccountNonLocked()
                        || !user.isCredentialsNonExpired())
                    throw RestException.restThrow(USER_PERMISSION_RESTRICTION);


                Timestamp tokenIssuedAt = new Timestamp(System.currentTimeMillis() / 1000 * 1000);
                String newAccessToken = jwtTokenProvider.generateAccessToken(user, tokenIssuedAt);
                String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

                user.setTokenIssuedAt(tokenIssuedAt.getTime());
                userRepository.save(user);

                TokenDTO tokenDTO = TokenDTO.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();
                return ApiResult.success(tokenDTO);
            } catch (Exception e) {
                throw RestException.restThrow(REFRESH_TOKEN_EXPIRED);
            }
        } catch (Exception ex) {
            throw RestException.restThrow(WRONG_ACCESS_TOKEN);
        }
        throw RestException.restThrow(ACCESS_TOKEN_NOT_EXPIRED);

    }

    private static String getTokenWithOutBearer(String token) {
        return token.trim();
    }

}
