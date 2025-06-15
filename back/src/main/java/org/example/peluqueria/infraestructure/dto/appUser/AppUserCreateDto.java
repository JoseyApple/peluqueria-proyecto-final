package org.example.peluqueria.infraestructure.dto.appUser;

import org.example.peluqueria.domain.models.AppUser;

public record AppUserCreateDto(
        String userName,
        String email,
        String password
) {

    public AppUser toEntity() {
        AppUser user = new AppUser();
        user.setUserName(this.userName);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setActive(true); // Por defecto activos
        user.setRole(org.example.peluqueria.domain.Role.USER); // Por defecto USER
        return user;
    }
}
