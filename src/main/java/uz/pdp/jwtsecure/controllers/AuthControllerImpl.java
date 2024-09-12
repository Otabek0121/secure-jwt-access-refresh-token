package uz.pdp.jwtsecure.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.pdp.jwtsecure.dtos.*;
import uz.pdp.jwtsecure.services.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllerImpl {

    private final AuthService authService;

    @PostMapping("login")
    private ApiResult<TokenDTO> login(@RequestBody SignInRequestDTO signInRequestDTO){
       return authService.login(signInRequestDTO);
    }

    @PostMapping("refresh-token")
    private ApiResult<TokenDTO> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO){
        return authService.refreshToken(refreshTokenDTO);
    }







}
