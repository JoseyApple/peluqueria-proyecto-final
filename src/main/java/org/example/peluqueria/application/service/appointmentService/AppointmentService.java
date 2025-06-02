package org.example.peluqueria.application.service.appointmentService;

import org.example.peluqueria.domain.AppointmentStatus;
import org.example.peluqueria.domain.models.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface AppointmentService {

    Appointment createAppointment(Appointment appointment);

    void changeAppointmentStatus(Long appointmentId, AppointmentStatus newStatus);

    Page<Appointment> getAppointmentsByClient(Long clientId, Pageable pageable);

    Page<Appointment> findall(Pageable pageable);

    int contarCitasPendientesDelDia(LocalDate fecha);
}
