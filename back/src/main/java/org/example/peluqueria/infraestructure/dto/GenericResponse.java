package org.example.peluqueria.infraestructure.dto;

import lombok.Builder;

@Builder
public record GenericResponse(int code, String message){
}
