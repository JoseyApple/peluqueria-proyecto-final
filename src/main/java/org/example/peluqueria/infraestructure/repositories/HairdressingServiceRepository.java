package org.example.peluqueria.infraestructure.repositories;

import org.example.peluqueria.domain.models.HairdressingService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HairdressingServiceRepository extends JpaRepository<HairdressingService, Long> {
}
