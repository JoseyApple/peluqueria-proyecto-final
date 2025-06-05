package org.example.peluqueria.infraestructure.dto.appointment;

import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;
import java.util.List;

public record CreateAppointmentDto(
        @Future(message = "La hora de inicio debe ser en el futuro")
        LocalDateTime startTime,
        Long clientId,
        List<Long> serviceIds) { }

