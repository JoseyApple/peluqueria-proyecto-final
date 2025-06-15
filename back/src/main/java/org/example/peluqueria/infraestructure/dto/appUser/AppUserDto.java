package org.example.peluqueria.infraestructure.dto.appUser;

import org.example.peluqueria.domain.Role;
import org.example.peluqueria.domain.models.AppUser;

import java.time.LocalDate;

public record AppUserDto(
        Long id,
        String userName,
        String email,
        LocalDate createdAt,
        Boolean active,
        Role role
) {
    public static AppUserDto fromEntity(AppUser user) {
        return new AppUserDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.isActive(),
                user.getRole()
        );
    }
}

