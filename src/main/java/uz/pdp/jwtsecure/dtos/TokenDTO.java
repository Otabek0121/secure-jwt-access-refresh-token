package uz.pdp.jwtsecure.dtos;

import lombok.*;
import uz.pdp.jwtsecure.utils.RestConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {

    private final String tokenType = RestConstants.TOKEN_TYPE;

    private String accessToken;

    private String refreshToken;
}
