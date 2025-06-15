package org.example.peluqueria.infraestructure.dto;

import lombok.Builder;

@Builder
public record TokenOutDto(int status, String token, String expiration) {
}
