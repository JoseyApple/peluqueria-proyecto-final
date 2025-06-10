package org.example.peluqueria.infraestructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.peluqueria.application.service.appointmentService.AppointmentService;
import org.example.peluqueria.application.service.appuser.AppUserService;
import org.example.peluqueria.application.service.order.OrderService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@Tag(name = "CITAS", description = "Operaciones relacionadas con citas")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppUserService appUserService;
    private final HairdressingServiceRepository hairdressingServiceRepository;
    private final SecurityUtils securityUtils;
    private final AppointmentCriteriaBuilder appointmentCriteriaBuilder;
    private final OrderService orderService;

    @PostMapping
    @Operation(
            summary = "Crear una cita",
            description = "Permite a un cliente o un administrador crear una nueva cita con los servicios seleccionados y la hora de inicio."
    )
    public ResponseEntity<AppointmentResponseDto> createAppointment(
            @Valid @RequestBody CreateAppointmentDto dto,
            @AuthenticationPrincipal UserPrincipal currentUser) {


        securityUtils.assertSameUserOrAdmin(currentUser, dto.clientId());

        AppUser client = appUserService.findById(dto.clientId());
        List<HairdressingService> services = hairdressingServiceRepository.findAllById(dto.serviceIds());

        Appointment appointment = new Appointment();
        appointment.setStartTime(dto.startTime());
        appointment.setClient(client);
        appointment.setServices(services);

        Appointment created = appointmentService.createAppointment(appointment);
        orderService.createOrder(created.getId());

        AppointmentResponseDto response = AppointmentResponseDto.fromEntity(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Buscar citas (ADMIN)",
            description = "Permite a un administrador buscar citas filtrando por fecha, hora, estado, correo del cliente, estado del pedido y monto mínimo del pedido, con paginación."
    )
    public ResponseEntity<PageOutDto<AppointmentResponseDto>> getPageOutDtoResponseEntity(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(required = false) BigDecimal minOrderTotal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String clientEmail
    ) {
        List<Appointment> appointments = appointmentCriteriaBuilder.findAllWithFilters(
                startDateTime, endDateTime, status, clientEmail, orderStatus, minOrderTotal, page, size
        );

        long totalElements = appointmentCriteriaBuilder.countWithFilters(
                startDateTime, endDateTime, status, clientEmail, orderStatus, minOrderTotal
        );

        List<AppointmentResponseDto> content = appointments.stream()
                .map(AppointmentResponseDto::fromEntity)
                .toList();

        PageOutDto<AppointmentResponseDto> response = new PageOutDto<>(
                page,
                size,
                totalElements,
                (int) Math.ceil((double) totalElements / size),
                content
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/my-appointments/search")
    @Operation(
            summary = "Buscar mis citas",
            description = "Permite a un usuario autenticado buscar sus propias citas con filtros por fecha, hora, estado, pedido y total mínimo, incluyendo paginación."
    )
    public ResponseEntity<PageOutDto<AppointmentResponseDto>> searchMyAppointments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(required = false) BigDecimal minOrderTotal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        String clientEmail = user.getUsername();

        return getPageOutDtoResponseEntity(
                startDateTime,
                endDateTime,
                status,
                orderStatus,
                minOrderTotal,
                page,
                size,
                clientEmail
        );
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Actualizar estado de una cita",
            description = "Permite modificar el estado de una cita existente (por ejemplo, cambiar a CONFIRMED o CANCELLED) mediante su ID."
    )
    public ResponseEntity<Void> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam String newStatus) {

        appointmentService.changeAppointmentStatus(id, newStatus);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pending-appointments/{date}")
    @Operation(
            summary = "Contar citas pendientes por día",
            description = "Devuelve el número de citas pendientes para una fecha específica proporcionada en el path."
    )
    public ResponseEntity<Integer> getPendingAppointments(@PathVariable LocalDate date) {
        return ResponseEntity.ok(appointmentService.contarCitasPendientesDelDia(date));
    }

    @GetMapping("/client-availability/{clientId}")
    @Operation(
            summary = "Verificar si el cliente tiene una cita hoy",
            description = "Devuelve true si el cliente ya tiene una cita pendiente o confirmada en la fecha actual, y false si está disponible para agendar."
    )
    public ResponseEntity<Boolean> tieneCitaHoy(@PathVariable Long clientId) {
        return ResponseEntity.ok(appointmentService.existenCitasHoy(clientId));
    }


}
