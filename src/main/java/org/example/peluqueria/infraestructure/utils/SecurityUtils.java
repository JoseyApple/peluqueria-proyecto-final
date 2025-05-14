package org.example.peluqueria.infraestructure.utils;

import org.example.peluqueria.exceptions.UserUnauthorizedException;
import org.example.peluqueria.infraestructure.security.UserPrincipal;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public boolean isAdmin(UserPrincipal user) {
        return user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public void assertSameUserOrAdmin(UserPrincipal user, Long requestedUserId) {
        if (!isAdmin(user) && !user.getId().equals(requestedUserId)) {
            throw new UserUnauthorizedException("Access denied for user ID: " + requestedUserId);
        }
    }

    public Long getRequesterIdIfNotAdmin(UserPrincipal user) {
        return isAdmin(user) ? null : user.getId();
    }
}
