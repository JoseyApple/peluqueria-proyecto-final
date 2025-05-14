package org.example.peluqueria.infraestructure.dto.appointment;

import java.time.LocalDateTime;
import java.util.List;

public record CreateAppointmentDto(LocalDateTime startTime, Long clientId, List<Long> serviceIds) { }

