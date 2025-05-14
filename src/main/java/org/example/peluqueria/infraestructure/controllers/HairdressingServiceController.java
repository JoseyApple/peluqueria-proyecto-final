package org.example.peluqueria.infraestructure.controllers;

import lombok.RequiredArgsConstructor;
import org.example.peluqueria.domain.models.HairdressingService;
import org.example.peluqueria.infraestructure.dto.hairdressingService.HairdressingServiceResponseDto;
import org.example.peluqueria.infraestructure.repositories.HairdressingServiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class HairdressingServiceController {

    private final HairdressingServiceRepository hairdressingServiceRepository;

    @GetMapping
    public ResponseEntity<List<HairdressingServiceResponseDto>> getAllServices() {
        List<HairdressingService> services = hairdressingServiceRepository.findAll();
        List<HairdressingServiceResponseDto> response = services.stream()
                .map(HairdressingServiceResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }
}
