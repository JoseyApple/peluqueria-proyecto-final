package org.example.peluqueria.infraestructure.controllers;

import lombok.RequiredArgsConstructor;
import org.example.peluqueria.application.service.appointmentService.AppointmentService;
import org.example.peluqueria.application.service.appuser.AppUserService;
import org.example.peluqueria.domain.AppointmentStatus;
import org.example.peluqueria.domain.OrderStatus;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.domain.models.Appointment;
import org.example.peluqueria.domain.models.HairdressingService;
import org.example.peluqueria.infraestructure.criteriabuilders.AppointmentCriteriaBuilder;
import org.example.peluqueria.infraestructure.dto.PageOutDto;
import org.example.peluqueria.infraestructure.dto.appointment.AppointmentResponseDto;
import org.example.peluqueria.infraestructure.dto.appointment.CreateAppointmentDto;
import org.example.peluqueria.infraestructure.repositories.HairdressingServiceRepository;
import org.example.peluqueria.infraestructure.security.UserPrincipal;
import org.example.peluqueria.infraestructure.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppUserService appUserService;
    private final HairdressingServiceRepository hairdressingServiceRepository;
    private final SecurityUtils securityUtils;
    private final AppointmentCriteriaBuilder appointmentCriteriaBuilder;

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> createAppointment(
            @RequestBody CreateAppointmentDto dto,
            @AuthenticationPrincipal UserPrincipal currentUser) {


        securityUtils.assertSameUserOrAdmin(currentUser, dto.clientId());

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


    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageOutDto<AppointmentResponseDto>> searchAppointments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startHour,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endHour,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) String clientEmail,
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(required = false) BigDecimal minOrderTotal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Obtener datos desde DAO
        return getPageOutDtoResponseEntity(startDate, endDate, startHour, endHour, status, orderStatus, minOrderTotal, page, size, clientEmail);
    }

    @GetMapping("/my-appointments/search")
    public ResponseEntity<PageOutDto<AppointmentResponseDto>> searchMyAppointments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startHour,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endHour,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(required = false) BigDecimal minOrderTotal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        String clientEmail = user.getUsername(); // El email del usuario autenticado

        return getPageOutDtoResponseEntity(startDate, endDate, startHour, endHour, status, orderStatus, minOrderTotal, page, size, clientEmail);
    }

    private ResponseEntity<PageOutDto<AppointmentResponseDto>> getPageOutDtoResponseEntity(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false) LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false) LocalDate endDate, @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @RequestParam(required = false) LocalTime startHour, @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @RequestParam(required = false) LocalTime endHour, @RequestParam(required = false) AppointmentStatus status, @RequestParam(required = false) OrderStatus orderStatus, @RequestParam(required = false) BigDecimal minOrderTotal, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, String clientEmail) {
        List<Appointment> appointments = appointmentCriteriaBuilder.findAllWithFilters(
                startDate, endDate, startHour, endHour, status, clientEmail, orderStatus, minOrderTotal,
                page, size
        );

        long totalElements = appointmentCriteriaBuilder.countWithFilters(
                startDate, endDate, startHour, endHour, status, clientEmail, orderStatus, minOrderTotal
        );

        List<AppointmentResponseDto> content = appointments.stream()
                .map(AppointmentResponseDto::fromEntity)
                .toList();

        PageOutDto<AppointmentResponseDto> response = new PageOutDto<>(
                page, size, totalElements, (int) Math.ceil((double) totalElements / size), content
        );

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus newStatus) {

        appointmentService.changeAppointmentStatus(id, newStatus);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pending-appointments/{date}")
    public ResponseEntity<Integer> getPendingAppointments(
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(appointmentService.contarCitasPendientesDelDia(date));
    }

    @GetMapping("/disponibilidad/{clientId}")
    public ResponseEntity<Boolean> tieneCitaHoy(@PathVariable Long clientId) {
        return ResponseEntity.ok(appointmentService.existenCitasHoy(clientId));
    }

}
