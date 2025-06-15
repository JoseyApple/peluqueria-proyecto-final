package org.example.peluqueria.infraestructure.dto.hairdressingService;

import org.example.peluqueria.domain.models.HairdressingService;

import java.math.BigDecimal;

public record HairdressingServiceResponseDto(
        Long id,
        String name,
        String description,
        Integer durationMinutes,
        BigDecimal price
) {
    public static HairdressingServiceResponseDto fromEntity(HairdressingService service) {
        return new HairdressingServiceResponseDto(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getDurationMinutes(),
                service.getPrice()
        );
    }
}
