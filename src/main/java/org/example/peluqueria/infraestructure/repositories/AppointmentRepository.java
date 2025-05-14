package org.example.peluqueria.infraestructure.repositories;

import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.domain.models.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByStartTimeLessThanAndEndTimeGreaterThan(LocalDateTime endTime, LocalDateTime startTime);
    Page<Appointment> getAppointmentsByClient(Long clientId, Pageable pageable);
    Page<Appointment> findByClient(AppUser client, Pageable pageable);
}
