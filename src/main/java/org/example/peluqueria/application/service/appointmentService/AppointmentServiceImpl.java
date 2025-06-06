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
import org.example.peluqueria.infraestructure.repositories.HairdressingServiceRepository;
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

        if (currentStatus.name().equalsIgnoreCase(newStatus)) {
            throw new IllegalStateException("La cita ya está en el estado solicitado.");
        }

        // Lógica de transición válida
        if (currentStatus == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("No se puede cambiar el estado de una cita cancelada.");
        }

        if (currentStatus == AppointmentStatus.COMPLETED && !newStatus.equalsIgnoreCase(AppointmentStatus.CANCELLED.name())) {
            throw new IllegalStateException("No se puede modificar una cita completada.");
        }

        appointment.setStatus(AppointmentStatus.valueOf(newStatus));
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
    public int contarCitasPendientesDelDia(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();         // 00:00
        LocalDateTime fin = fecha.atTime(23, 59, 59);        // 23:59:59

        return appointmentRepository.countAllByStatusAndStartTimeBetween(
                AppointmentStatus.PENDING, inicio, fin
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
