package org.example.peluqueria.application.service.appointmentService;

import lombok.AllArgsConstructor;
import org.example.peluqueria.application.service.appuser.AppUserService;
import org.example.peluqueria.domain.AppointmentStatus;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.domain.models.Appointment;
import org.example.peluqueria.domain.models.HairdressingService;
import org.example.peluqueria.exceptions.EntityNotFoundException;
import org.example.peluqueria.infraestructure.repositories.AppointmentRepository;
import org.example.peluqueria.infraestructure.repositories.HairdressingServiceRepository;
import org.example.peluqueria.infraestructure.utils.TimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppUserService appUserService;
    private final HairdressingServiceRepository hairdressingServiceRepository;

    @Override
    public Appointment createAppointment(Appointment appointment) {
        LocalDateTime startDateTime = appointment.getStartTime();

        if (appointment.getServices() == null || appointment.getServices().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un servicio.");
        }
        System.out.println(appointment.getServices().size());

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
        appointment.setStatus(AppointmentStatus.RESERVED);

        // Guardar la cita en base de datos
        return appointmentRepository.save(appointment);
    }

    @Override
    public void cancelAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment no encontrado."));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("La cita ya está cancelada.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
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
    public void completeAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment no encontrado."));


        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("La cita ya está marcada como completada.");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }
}
