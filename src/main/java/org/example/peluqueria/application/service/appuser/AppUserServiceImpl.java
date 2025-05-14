package org.example.peluqueria.application.service.appuser;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.exceptions.AppUserException;
import org.example.peluqueria.exceptions.EmailAlreadyExistsException;
import org.example.peluqueria.exceptions.UserNotFoundException;
import org.example.peluqueria.infraestructure.dto.appUser.UserLoginDto;
import org.example.peluqueria.infraestructure.repositories.AppUserRepository;
import org.example.peluqueria.infraestructure.security.JwtService;
import org.example.peluqueria.infraestructure.security.UserPrincipal;
import org.example.peluqueria.infraestructure.utils.CredentialUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser findById(Long id) {

        return appUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with ID: " + id + " not found"));
    }

    @Transactional
    @Override
    public AppUser createUser(AppUser appUser) {

        CredentialUtils.validateEmail(appUser.getEmail());
        CredentialUtils.validatePassword(appUser.getPassword());

        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(appUser.getEmail());

        if (appUserOptional.isPresent()) {

            throw new EmailAlreadyExistsException("User with email: " + appUser.getEmail() + " already exists");
        }

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setCreatedAt(LocalDate.now());

        return appUserRepository.save(appUser);

    }


    @Override
    public String loginUser(UserLoginDto dto) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            // AuthenticationManager ya devolvió un UsernamePasswordAuthenticationToken
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            // Genera el token usando el principal directamente
            return jwtService.generateToken(principal);

        } catch (AuthenticationException ex) {
            throw new AppUserException("Incorrect email or password");
        }
    }

    @Transactional
    @Override
    public void statusChanger(Long id, Boolean active) {

        AppUser userFound = findById(id);

        userFound.setActive(active);

        appUserRepository.save(userFound);

    }

    @Override
    public Page<AppUser> findAll(Pageable pageable) {

        return appUserRepository.findAllByActiveIsTrue(pageable);
    }

}
