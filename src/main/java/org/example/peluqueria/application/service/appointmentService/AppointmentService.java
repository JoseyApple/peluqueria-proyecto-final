package org.example.peluqueria.application.service.appointmentService;

import org.example.peluqueria.domain.models.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {

    Appointment createAppointment(Appointment appointment);

    void cancelAppointment(Long appointmentId);

    Page<Appointment> getAppointmentsByClient(Long clientId, Pageable pageable);
}
