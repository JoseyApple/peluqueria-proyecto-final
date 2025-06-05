package org.example.peluqueria.infraestructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.peluqueria.application.service.appuser.AppUserService;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.infraestructure.dto.PageOutDto;
import org.example.peluqueria.infraestructure.dto.appUser.AppUserCreateDto;
import org.example.peluqueria.infraestructure.dto.appUser.AppUserDto;
import org.example.peluqueria.infraestructure.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "USUARIOS", description = "Operaciones relacionadas con usuarios")
public class AppUserController {

    private final AppUserService appUserService;

    @Operation(
            summary = "Obtener usuario por ID",
            description = "Devuelve la información de un usuario específico dado su ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getUserById(@PathVariable Long id) {
        AppUser user = appUserService.findById(id);
        return ResponseEntity.ok(AppUserDto.fromEntity(user));
    }


    @Operation(
            summary = "Crear nuevo usuario",
            description = "Registra un nuevo usuario en el sistema."
    )
    @PostMapping
    public ResponseEntity<AppUserDto> createUser(@RequestBody AppUserCreateDto dto) {
        AppUser createdUser = appUserService.createUser(dto.toEntity());
        return new ResponseEntity<>(AppUserDto.fromEntity(createdUser), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Cambiar estado del usuario",
            description = "Activa o desactiva un usuario dado su ID."
    )
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado del usuario: true (activo), false (inactivo)")
            @RequestParam Boolean active) {
        appUserService.statusChanger(id, active);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Listar todos los usuarios",
            description = "Devuelve una lista paginada de todos los usuarios registrados en el sistema."
    )
    @GetMapping
    public ResponseEntity<PageOutDto<AppUserDto>> getAllUsers(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        Page<AppUser> usersPage = appUserService.findAll(PageRequest.of(page, size));
        PageOutDto<AppUserDto> response = new PageOutDto<>(
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages(),
                usersPage.map(AppUserDto::fromEntity).getContent()
        );
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Obtener información del usuario actual",
            description = "Devuelve los datos del usuario autenticado actualmente."
    )
    @GetMapping("/me")
    public ResponseEntity<AppUserDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        AppUser user = appUserService.findById(principal.getId());
        return ResponseEntity.ok(AppUserDto.fromEntity(user));
    }

}

