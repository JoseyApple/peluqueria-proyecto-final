package org.example.peluqueria.infraestructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorOutDto(LocalDateTime timestamp, int status, String error, List<String> errors) {

}
