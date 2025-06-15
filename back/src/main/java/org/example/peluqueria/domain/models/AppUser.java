package org.example.peluqueria.domain.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.peluqueria.domain.Role;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDate createdAt;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private Role role;

}
