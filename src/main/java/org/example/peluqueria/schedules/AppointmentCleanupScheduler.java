package org.example.peluqueria.schedules;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.juli.logging.Log;
import org.example.peluqueria.domain.AppointmentStatus;
import org.example.peluqueria.infraestructure.repositories.AppointmentRepository;
import org.example.peluqueria.infraestructure.repositories.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.example.peluqueria.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentCleanupScheduler {

    private final AppointmentRepository appointmentRepository;
    private final OrderRepository orderRepository;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void marcarCitasVencidas() {
        int actualizadas = appointmentRepository.marcarComoExpiradas(
                AppointmentStatus.PENDING,
                AppointmentStatus.EXPIRED,
                LocalDateTime.now()
        );
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void marcarOrdenesVencidas() {
        int actualizadas = orderRepository.actualizarEstadoOrdenesVinculadas(
                OrderStatus.PENDING_PAYMENT,
                OrderStatus.CANCELLED,
                List.of(AppointmentStatus.PENDING, AppointmentStatus.EXPIRED, AppointmentStatus.CANCELLED),
                LocalDateTime.now()
        );
    }





}

