package org.example.peluqueria.infraestructure.dto.appointment;

import org.example.peluqueria.domain.models.Appointment;
import org.example.peluqueria.domain.models.HairdressingService;

import java.time.LocalDateTime;
import java.util.List;

public record AppointmentResponseDto(Long id, LocalDateTime startTime, LocalDateTime endTime, List<String> services, String status) {

    public static AppointmentResponseDto fromEntity(Appointment appointment) {
        List<String> serviceNames = appointment.getServices()
                .stream()
                .map(HairdressingService::getName)
                .toList();

        return new AppointmentResponseDto(
                appointment.getId(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                serviceNames,
                appointment.getStatus().name()
        );
    }
}
