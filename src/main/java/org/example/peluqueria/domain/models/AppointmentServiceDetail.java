package org.example.peluqueria.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AppointmentServiceDetail {
    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
    private String precio;
    private Integer duracionMinutos;

    @ManyToOne
    private Appointment appointment;

    @ManyToOne
    private HairdressingService servicioBase;
}
