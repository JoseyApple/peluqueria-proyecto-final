package org.example.peluqueria.infraestructure.dto.appointment;


import org.example.peluqueria.domain.models.Appointment;

import java.time.LocalDateTime;

public record AppointmentResponseDto2(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String status
) {
    public static AppointmentResponseDto2 fromEntity(Appointment appointment) {
        return new AppointmentResponseDto2(
                appointment.getId(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getStatus().name()
        );
    }
}

