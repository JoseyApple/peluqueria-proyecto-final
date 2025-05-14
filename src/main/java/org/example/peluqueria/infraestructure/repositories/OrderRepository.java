package org.example.peluqueria.infraestructure.repositories;

import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.domain.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByClient(AppUser client, Pageable pageable);
}
