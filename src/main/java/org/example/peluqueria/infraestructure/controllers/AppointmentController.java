package org.example.peluqueria.infraestructure.controllers;

import lombok.RequiredArgsConstructor;
import org.example.peluqueria.application.service.appointmentService.AppointmentService;
import org.example.peluqueria.application.service.appuser.AppUserService;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.domain.models.Appointment;
import org.example.peluqueria.domain.models.HairdressingService;
import org.example.peluqueria.infraestructure.dto.PageOutDto;
import org.example.peluqueria.infraestructure.dto.appointment.AppointmentResponseDto;
import org.example.peluqueria.infraestructure.dto.appointment.CreateAppointmentDto;
import org.example.peluqueria.infraestructure.repositories.HairdressingServiceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppUserService appUserService;
    private final HairdressingServiceRepository hairdressingServiceRepository;

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> createAppointment(@RequestBody CreateAppointmentDto dto) {

        AppUser client = appUserService.findById(dto.clientId());
        List<HairdressingService> services = hairdressingServiceRepository.findAllById(dto.serviceIds());

        Appointment appointment = new Appointment();
        appointment.setStartTime(dto.startTime());
        appointment.setClient(client);
        appointment.setServices(services);

        Appointment created = appointmentService.createAppointment(appointment);

        AppointmentResponseDto response = AppointmentResponseDto.fromEntity(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<PageOutDto<AppointmentResponseDto>> getAppointmentsByClient(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());
        Page<Appointment> appointmentPage = appointmentService.getAppointmentsByClient(clientId, pageable);

        List<AppointmentResponseDto> content = appointmentPage.getContent()
                .stream()
                .map(AppointmentResponseDto::fromEntity)
                .toList();

        PageOutDto<AppointmentResponseDto> response = new PageOutDto<>(
                appointmentPage.getNumber(),
                appointmentPage.getSize(),
                appointmentPage.getTotalElements(),
                appointmentPage.getTotalPages(),
                content
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }
}
