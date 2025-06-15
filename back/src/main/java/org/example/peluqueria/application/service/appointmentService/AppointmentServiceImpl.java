package org.example.peluqueria.application.service.appointmentService;

import lombok.AllArgsConstructor;
import org.example.peluqueria.application.service.appuser.AppUserService;
import org.example.peluqueria.domain.AppointmentStatus;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.domain.models.Appointment;
import org.example.peluqueria.domain.models.HairdressingService;
import org.example.peluqueria.exceptions.EntityNotFoundException;
import org.example.peluqueria.exceptions.InvalidAppointmentStatusException;
import org.example.peluqueria.infraestructure.repositories.AppointmentRepository;
import org.example.peluqueria.infraestructure.utils.TimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppUserService appUserService;
    private static final List<String> appointmentStatuses = List.of(AppointmentStatus.PENDING.name(),
            AppointmentStatus.CANCELLED.name(), AppointmentStatus.EXPIRED.name(),
            AppointmentStatus.COMPLETED.name(), AppointmentStatus.CONFIRMED.name());

    @Override
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }


    @Override
    public Appointment createAppointment(Appointment appointment) {
        LocalDateTime startDateTime = appointment.getStartTime();

        if (appointment.getServices() == null || appointment.getServices().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un servicio.");
        }

        // Calcular duración total de los servicios
        int totalDurationMinutes = appointment.getServices()
                .stream()
                .mapToInt(HairdressingService::getDurationMinutes)
                .sum();

        // Calcular hora de finalización
        LocalDateTime endDateTime = startDateTime.plusMinutes(totalDurationMinutes);
        appointment.setEndTime(endDateTime);

        // Validar que la cita esté dentro del horario permitido (mañana/tarde)
        LocalTime startTime = startDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();

        if (!TimeUtils.isWithinWorkingHours(startTime, endTime)) {
            throw new IllegalArgumentException("La cita está fuera del horario de apertura.");
        }

        // Validar conflictos con otras citas existentes (ignorando las canceladas)
        boolean conflict = appointmentRepository.existsByStartTimeLessThanAndEndTimeGreaterThanAndStatusNot(
                endDateTime, startDateTime, AppointmentStatus.CANCELLED);

        if (conflict) {
            throw new IllegalArgumentException("Conflicto de horarios. Ya existe una cita en ese rango.");
        }

        // Establecer estado inicial
        appointment.setStatus(AppointmentStatus.PENDING);

        // Guardar la cita en base de datos
        return appointmentRepository.save(appointment);
    }

    @Override
    public void changeAppointmentStatus(Long appointmentId, String newStatus) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada."));

        if (!appointmentStatuses.contains(newStatus.toUpperCase())) {
            throw new InvalidAppointmentStatusException("El estado de cita indicado no existe");
        }

        AppointmentStatus currentStatus = appointment.getStatus();
        AppointmentStatus requestedStatus = AppointmentStatus.valueOf(newStatus.toUpperCase());

        if (currentStatus == requestedStatus) {
            throw new IllegalStateException("La cita ya está en el estado solicitado.");
        }

        // ❌ No permitir volver a activar una cita cancelada
        if (currentStatus == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("No se puede modificar una cita que ya fue cancelada.");
        }

        // ❌ No permitir modificar una cita completada (salvo cancelarla)
        if (currentStatus == AppointmentStatus.COMPLETED && requestedStatus != AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("No se puede modificar una cita completada.");
        }

        // ✅ Solo actualizar el estado
        appointment.setStatus(requestedStatus);
        appointmentRepository.save(appointment);
    }

    @Override
    public Page<Appointment> getAppointmentsByClient(Long clientId, Pageable pageable) {
        AppUser client = appUserService.findById(clientId);
        return appointmentRepository.findByClient(client, pageable);
    }

    @Override
    public Page<Appointment> findall(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    @Override
    public int contarCitasPendientesDesde(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay(); // 00:00 del día dado

        return appointmentRepository.countAllByStatusAndStartTimeAfter(
                AppointmentStatus.PENDING, inicio
        );
    }

    @Override
    public boolean existenCitasHoy(Long clientId) {
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDate.now().atTime(LocalTime.MAX);

        List<AppointmentStatus> estadosBloqueantes = List.of(
                AppointmentStatus.PENDING,
                AppointmentStatus.CONFIRMED
        );

        return appointmentRepository.existsByClientIdAndStartTimeBetweenAndStatusIn(
                clientId, inicio, fin, estadosBloqueantes
        );
    }

}
