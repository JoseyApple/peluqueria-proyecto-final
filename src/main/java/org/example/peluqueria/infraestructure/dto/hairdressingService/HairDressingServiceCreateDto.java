package org.example.peluqueria.infraestructure.dto.hairdressingService;

import org.example.peluqueria.domain.models.HairdressingService;

import java.math.BigDecimal;

public record HairDressingServiceCreateDto(
        String name,
        String description,
        Integer durationMinutes,
        BigDecimal price
) {

    public HairdressingService toEntity() {
        HairdressingService service = new HairdressingService();
        service.setName(this.name);
        service.setDescription(this.description);
        service.setDurationMinutes(this.durationMinutes);
        service.setPrice(this.price);
        return service;
    }
}

