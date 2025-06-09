package org.example.peluqueria.application.service.order;

import lombok.RequiredArgsConstructor;
import org.example.peluqueria.application.service.appuser.AppUserService;
import org.example.peluqueria.domain.OrderStatus;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.domain.models.Appointment;
import org.example.peluqueria.domain.models.HairdressingService;
import org.example.peluqueria.domain.models.Order;
import org.example.peluqueria.exceptions.EntityNotFoundException;
import org.example.peluqueria.exceptions.InvalidOrderStatusCondition;
import org.example.peluqueria.infraestructure.repositories.AppointmentRepository;
import org.example.peluqueria.infraestructure.repositories.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppUserService appUserService;
    private final static List<String> ordersStatus = List.of(OrderStatus.PENDING_PAYMENT.name(),OrderStatus.CANCELLED.name(),OrderStatus.PAID.name());

    @Override
    public Order createOrder(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada."));

        if (appointment.getOrder() != null) {
            throw new IllegalStateException("Ya existe una factura para esta cita.");
        }

        BigDecimal totalAmount = appointment.getServices()
                .stream()
                .map(HairdressingService::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setClient(appointment.getClient());
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setAppointment(appointment);

        Order savedOrder = orderRepository.save(order);

        appointment.setOrder(savedOrder);
        appointmentRepository.save(appointment);

        return savedOrder;
    }

    @Override
    public void changeOrderStatus(Long orderId, String newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada."));

        if (!ordersStatus.contains(newStatus.toUpperCase())) {

            throw new InvalidOrderStatusCondition("El estado no es correcto.");
        }

        if (order.getStatus().name().equalsIgnoreCase(newStatus)) {

            throw new InvalidOrderStatusCondition("La factura ya esta: " + newStatus.toUpperCase());
        }

        order.setStatus(OrderStatus.valueOf(newStatus.toUpperCase()));

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

