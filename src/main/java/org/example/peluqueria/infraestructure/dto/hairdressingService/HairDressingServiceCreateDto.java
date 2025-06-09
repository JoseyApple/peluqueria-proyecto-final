package org.example.peluqueria.infraestructure.dto.hairdressingService;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.peluqueria.domain.models.HairdressingService;

import java.math.BigDecimal;

public record HairDressingServiceCreateDto(

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotBlank(message = "La descripción es obligatoria")
        String description,

        @NotNull(message = "La duración es obligatoria")
        @Min(value = 1, message = "La duración debe ser mayor a 0 minutos")
        Integer durationMinutes,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
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



