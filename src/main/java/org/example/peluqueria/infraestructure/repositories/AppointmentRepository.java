package org.example.peluqueria.infraestructure.repositories;

import org.example.peluqueria.domain.AppointmentStatus;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.domain.models.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByStartTimeLessThanAndEndTimeGreaterThan(LocalDateTime endTime, LocalDateTime startTime);
    Page<Appointment> getAppointmentsByClient(Long clientId, Pageable pageable);
    Page<Appointment> findByClient(AppUser client, Pageable pageable);
    boolean existsByStartTimeLessThanAndEndTimeGreaterThanAndStatusNot(
            LocalDateTime endTime, LocalDateTime startTime, AppointmentStatus status);

    int countAllByStatusAndStartTimeBetween(AppointmentStatus status, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("UPDATE Appointment a SET a.status = :newStatus WHERE a.status = :oldStatus AND a.startTime < :ahora")
    int marcarComoExpiradas(@Param("oldStatus") AppointmentStatus oldStatus,
                            @Param("newStatus") AppointmentStatus newStatus,
                            @Param("ahora") LocalDateTime ahora);


}
