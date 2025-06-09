package org.example.peluqueria.infraestructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.peluqueria.application.service.hairdressing.HairDressingServiceInterface;
import org.example.peluqueria.domain.models.HairdressingService;
import org.example.peluqueria.infraestructure.dto.PageOutDto;
import org.example.peluqueria.infraestructure.dto.hairdressingService.HairDressingServiceCreateDto;
import org.example.peluqueria.infraestructure.dto.hairdressingService.HairdressingServiceResponseDto;
import org.example.peluqueria.infraestructure.repositories.HairdressingServiceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
@Tag(name = "Servicios de peluquería", description = "Operaciones relacionadas con servicios de peluquería")
public class HairdressingServiceController {

    private final HairDressingServiceInterface hairdressingService;

    @GetMapping
    @Operation(
            summary = "Obtener servicios de peluquería paginados",
            description = "Devuelve una lista paginada de los servicios de peluquería disponibles. Se puede especificar número de página y tamaño."
    )
    public ResponseEntity<PageOutDto<HairdressingServiceResponseDto>> getAllServices(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        Page<HairdressingService> servicePage = hairdressingService.findAll(PageRequest.of(page, size));
        PageOutDto<HairdressingServiceResponseDto> response = new PageOutDto<>(
                servicePage.getNumber(),
                servicePage.getSize(),
                servicePage.getTotalElements(),
                servicePage.getTotalPages(),
                servicePage.map(HairdressingServiceResponseDto::fromEntity).getContent()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener un servicio por ID",
            description = "Devuelve un servicio de peluquería específico mediante su ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<HairdressingServiceResponseDto> getServiceById(@PathVariable Long id) {
        HairdressingService service = hairdressingService.findById(id);
        return ResponseEntity.ok(HairdressingServiceResponseDto.fromEntity(service));
    }

    @Operation(
            summary = "Crear un nuevo servicio de peluquería",
            description = "Permite registrar un nuevo servicio de peluquería si no existe uno con el mismo nombre."
    )
    @PostMapping
    public ResponseEntity<HairdressingServiceResponseDto> createService(@Valid @RequestBody HairDressingServiceCreateDto dto) {
        HairdressingService service = dto.toEntity();
        HairdressingService saved = hairdressingService.save(service);
        return new ResponseEntity<>(HairdressingServiceResponseDto.fromEntity(saved), HttpStatus.CREATED);
    }



}
