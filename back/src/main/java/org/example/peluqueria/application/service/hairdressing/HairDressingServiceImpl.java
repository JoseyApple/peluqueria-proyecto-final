package org.example.peluqueria.application.service.hairdressing;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.peluqueria.domain.models.HairdressingService;
import org.example.peluqueria.exceptions.EntityExistsException;
import org.example.peluqueria.exceptions.EntityNotFoundException;
import org.example.peluqueria.infraestructure.repositories.HairdressingServiceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class HairDressingServiceImpl implements HairDressingServiceInterface{

    private final HairdressingServiceRepository hairdressingServiceRepository;

    @Override
    public HairdressingService findById(Long id) {

        return hairdressingServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No se encontro el servicio de peluquería con id: " + id));
    }

    @Transactional
    @Override
    public HairdressingService save(HairdressingService service) {

        Optional<HairdressingService> serviceFound = Optional.ofNullable(hairdressingServiceRepository.findByName(service.getName()));

        if (serviceFound.isPresent()) {

            throw new EntityExistsException("El servicio ya existe");
        }

        return hairdressingServiceRepository.save(service);
    }

    @Override
    public Page<HairdressingService> findAll(Pageable page) {

        return hairdressingServiceRepository.findAll(page);
    }

}
