package org.example.peluqueria.infraestructure.dto.appUser;

import org.example.peluqueria.domain.Role;
import org.example.peluqueria.domain.models.AppUser;

import java.time.LocalDate;

public record AppUserDto(
        Long id,
        String userName,
        String email,
        String password,
        LocalDate createdAt,
        Boolean active,
        Role role
) {

    public static AppUserDto fromEntity(AppUser user) {
        return new AppUserDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                null,
                user.getCreatedAt(),
                user.isActive(),
                user.getRole()
        );
    }

    public AppUser toEntity() {
        AppUser user = new AppUser();
        user.setUserName(this.userName);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setActive(this.active != null ? this.active : true);
        user.setRole(this.role != null ? this.role : Role.USER);
        return user;
    }
}
