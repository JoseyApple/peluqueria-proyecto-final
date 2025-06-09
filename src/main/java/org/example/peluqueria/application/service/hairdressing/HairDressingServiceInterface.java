package org.example.peluqueria.application.service.hairdressing;

import jakarta.transaction.Transactional;
import org.example.peluqueria.domain.models.HairdressingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HairDressingServiceInterface {
    HairdressingService findById(Long id);

    @Transactional
    HairdressingService save(HairdressingService service);

    Page<HairdressingService> findAll(Pageable page);
}
