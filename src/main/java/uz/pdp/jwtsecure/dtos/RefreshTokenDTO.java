package uz.pdp.jwtsecure.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDTO {

    @NotBlank
    private String refreshToken;

    @NotBlank
    private String accessToken;
}
