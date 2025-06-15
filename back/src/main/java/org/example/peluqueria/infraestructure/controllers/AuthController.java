package org.example.peluqueria.infraestructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.peluqueria.application.service.appuser.AppUserService;
import org.example.peluqueria.infraestructure.dto.TokenOutDto;
import org.example.peluqueria.infraestructure.dto.appUser.UserLoginDto;
import org.example.peluqueria.infraestructure.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "LOGIN", description = "Iniciar sesión en la aplicación")
public class AuthController {

    private final JwtService jwtService;
    private final AppUserService appUserService;

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Permite a un usuario autenticarse con sus credenciales y obtener un token JWT para acceder a los recursos protegidos."
    )
    public ResponseEntity<TokenOutDto> loginUser(@Valid @RequestBody UserLoginDto dto) {
        String token = appUserService.loginUser(dto);

        return ResponseEntity
                .ok()
                .body(new TokenOutDto(HttpStatus.OK.value(), token, jwtService.getExpiration()));
    }

}
