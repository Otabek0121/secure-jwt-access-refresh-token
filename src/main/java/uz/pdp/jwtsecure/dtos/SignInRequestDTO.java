package uz.pdp.jwtsecure.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import uz.pdp.jwtsecure.utils.MessageConstants;


@Getter
@Setter
public class SignInRequestDTO {

    @NotBlank(message = MessageConstants.USERNAME_CAN_NOT_BE_EMPTY)
    private String username;

    @NotBlank(message = MessageConstants.PASSWORD_CAN_NOT_BE_EMPTY)
    private String password;
}
