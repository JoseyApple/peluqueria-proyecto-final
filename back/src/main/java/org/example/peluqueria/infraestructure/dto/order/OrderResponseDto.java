package org.example.peluqueria.infraestructure.dto.order;

import org.example.peluqueria.domain.models.Order;

import java.math.BigDecimal;

public record OrderResponseDto(Long id, BigDecimal totalAmount, String status) {

    public static OrderResponseDto fromEntity(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus().name()
        );
    }
}
