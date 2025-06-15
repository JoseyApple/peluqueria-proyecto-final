package org.example.peluqueria.domain.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.peluqueria.domain.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    private AppUser client;

    @ManyToMany
    private List<HairdressingService> services;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @OneToOne
    private Order order;
}
