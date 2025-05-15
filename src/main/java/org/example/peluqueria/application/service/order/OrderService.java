package org.example.peluqueria.application.service.order;

import org.example.peluqueria.domain.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Order createOrder(Long appointmentId);

    void markOrderAsPaid(Long orderId);

    Page<Order> getOrdersByClient(Long clientId, Pageable pageable);

    Order getOrderByAppIdAndOrderId(Long appId, Long orderId);

    Page<Order> findAllorders(Pageable pageable);
}
