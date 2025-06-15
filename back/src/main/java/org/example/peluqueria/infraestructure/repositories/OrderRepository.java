package org.example.peluqueria.infraestructure.repositories;

import org.example.peluqueria.domain.AppointmentStatus;
import org.example.peluqueria.domain.OrderStatus;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.domain.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByClient(AppUser client, Pageable pageable);
    Order findByIdAndClient_Id(Long id, Long clientId);

    @Modifying
    @Query("""
    UPDATE Order o
    SET o.status = :nuevoEstado
    WHERE o.status = :estadoActual
    AND EXISTS (
        SELECT a FROM Appointment a
        WHERE a.order = o
        AND a.status IN :estadosCita
        AND a.startTime < :ahora
    )
""")
    int actualizarEstadoOrdenesVinculadas(
            @Param("estadoActual") OrderStatus estadoActual,
            @Param("nuevoEstado") OrderStatus nuevoEstado,
            @Param("estadosCita") List<AppointmentStatus> estadosCita,
            @Param("ahora") LocalDateTime ahora
    );


}
