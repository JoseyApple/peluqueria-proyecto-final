package org.example.peluqueria.infraestructure.repositories;

import org.example.peluqueria.domain.models.AppointmentServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentServiceDetailRepository extends JpaRepository<AppointmentServiceDetail, Long> {
}

