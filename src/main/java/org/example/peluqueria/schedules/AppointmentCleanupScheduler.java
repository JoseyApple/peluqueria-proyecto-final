package org.example.peluqueria.schedules;

import lombok.AllArgsConstructor;
import org.example.peluqueria.domain.AppointmentStatus;
import org.example.peluqueria.infraestructure.repositories.AppointmentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AppointmentCleanupScheduler {

    private final AppointmentRepository appointmentRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void marcarCitasVencidas() {
        int actualizadas = appointmentRepository.marcarComoExpiradas(
                AppointmentStatus.PENDING,
                AppointmentStatus.EXPIRED,
                LocalDateTime.now()
        );
    }

}

