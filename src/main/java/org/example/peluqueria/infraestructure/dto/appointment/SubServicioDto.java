package org.example.peluqueria.infraestructure.dto.appointment;

import jakarta.annotation.Nullable;

public record SubServicioDto(
        String nombre,
        Integer duracionMinutos,
        String precio,
        @Nullable Long servicioBaseId
) { }
