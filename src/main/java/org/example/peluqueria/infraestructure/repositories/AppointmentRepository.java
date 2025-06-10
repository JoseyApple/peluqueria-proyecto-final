package org.example.peluqueria.infraestructure.repositories;

import jakarta.transaction.Transactional;
import org.example.peluqueria.domain.AppointmentStatus;
import org.example.peluqueria.domain.OrderStatus;
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
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

        Page<Appointment> findByClient(AppUser client, Pageable pageable);
    boolean existsByStartTimeLessThanAndEndTimeGreaterThanAndStatusNot(
            LocalDateTime endTime, LocalDateTime startTime, AppointmentStatus status);

    int countAllByStatusAndStartTimeBetween(AppointmentStatus status, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("""
    UPDATE Appointment a
    SET a.status = :nuevoEstado
    WHERE a.status = :estadoActual AND a.startTime < :fechaHoraLimite
""")
    int marcarComoExpiradas(
            @Param("estadoActual") AppointmentStatus estadoActual,
            @Param("nuevoEstado") AppointmentStatus nuevoEstado,
            @Param("fechaHoraLimite") LocalDateTime fechaHoraLimite
    );


    boolean existsByClientIdAndStartTimeBetweenAndStatusIn(
            Long clientId,
            LocalDateTime start,
            LocalDateTime end,
            List<AppointmentStatus> statuses
    );

    List<Appointment> findAllByStartTimeBetween(LocalDateTime start, LocalDateTime end);

}
