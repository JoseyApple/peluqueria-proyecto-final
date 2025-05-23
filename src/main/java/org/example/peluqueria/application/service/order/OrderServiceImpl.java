package org.example.peluqueria.application.service.order;

import lombok.RequiredArgsConstructor;
import org.example.peluqueria.application.service.appuser.AppUserService;
import org.example.peluqueria.domain.OrderStatus;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.domain.models.Appointment;
import org.example.peluqueria.domain.models.HairdressingService;
import org.example.peluqueria.domain.models.Order;
import org.example.peluqueria.exceptions.EntityNotFoundException;
import org.example.peluqueria.infraestructure.repositories.AppointmentRepository;
import org.example.peluqueria.infraestructure.repositories.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppUserService appUserService;

    @Override
    public Order createOrder(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada."));

        if (appointment.getOrder() != null) {
            throw new IllegalStateException("Ya existe un pago para esta cita.");
        }

        BigDecimal totalAmount = appointment.getServices()
                .stream()
                .map(HairdressingService::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setClient(appointment.getClient());
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        // Asociar el Order a la cita
        appointment.setOrder(savedOrder);
        appointmentRepository.save(appointment);

        return savedOrder;
    }

    @Override
    public void markOrderAsPaid(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado."));

        if (order.getStatus() == OrderStatus.PAID) {
            throw new IllegalStateException("El pedido ya está pagado.");
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada."));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("La orden ya está cancelada.");
        }

        if (order.getStatus() == OrderStatus.PAID) {
            throw new IllegalStateException("No se puede cancelar una orden ya pagada.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }


    @Override
    public Page<Order> getOrdersByClient(Long clientId, Pageable pageable) {
        AppUser client = appUserService.findById(clientId);
        return orderRepository.findByClient(client, pageable);
    }

    @Override
    public Order getOrderByAppIdAndOrderId(Long appId, Long orderId) {
        Order order = orderRepository.findByIdAndClient_Id(orderId, appId);
        if (order == null) {
            throw new EntityNotFoundException("Order not found with id: " + orderId + " and clientId: " + appId);
        }
        return order;
    }

    @Override
    public Page<Order> findAllorders(Pageable pageable) {

        return orderRepository.findAll(pageable);
    }
}

