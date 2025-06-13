package org.example.peluqueria.infraestructure.dto.appointment;

import jakarta.validation.constraints.Future;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public record CreateAppointmentDto(
        @Future(message = "La hora de inicio debe ser en el futuro")
        LocalDateTime startTime,
        Long clientId,
        List<Long> serviceIds,

        @Nullable  // ✅ Este campo será opcional
        LocalDateTime endTime
) { }
