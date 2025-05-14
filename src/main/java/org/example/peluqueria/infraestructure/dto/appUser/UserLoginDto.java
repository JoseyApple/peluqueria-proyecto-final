package org.example.peluqueria.infraestructure.dto.appUser;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {

    @NotBlank(message = "You need email in order to log in!")
    private String email;
    @NotBlank(message = "You need password in order to log in!")
    private String password;
}
