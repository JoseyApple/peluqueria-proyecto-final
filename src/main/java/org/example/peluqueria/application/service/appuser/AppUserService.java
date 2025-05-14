package org.example.peluqueria.application.service.appuser;


import jakarta.transaction.Transactional;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.infraestructure.dto.appUser.UserLoginDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppUserService {


    AppUser findById(Long id);

    AppUser createUser(AppUser appUser);

    String loginUser(UserLoginDto dto);

    void statusChanger(Long id, Boolean active);

    Page<AppUser> findAll(Pageable pageable);
}
