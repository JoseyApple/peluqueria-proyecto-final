package org.example.peluqueria.infraestructure.controllers;

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
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getUserById(@PathVariable Long id) {
        AppUser user = appUserService.findById(id);
        return ResponseEntity.ok(AppUserDto.fromEntity(user));
    }

    @PostMapping
    public ResponseEntity<AppUserDto> createUser(@RequestBody AppUserCreateDto dto) {
        AppUser createdUser = appUserService.createUser(dto.toEntity());
        return new ResponseEntity<>(AppUserDto.fromEntity(createdUser), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id, @RequestParam Boolean active) {
        appUserService.statusChanger(id, active);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageOutDto<AppUserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
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

    @GetMapping("/me")
    public ResponseEntity<AppUserDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        AppUser user = appUserService.findById(principal.getId());
        return ResponseEntity.ok(AppUserDto.fromEntity(user));
    }
}

