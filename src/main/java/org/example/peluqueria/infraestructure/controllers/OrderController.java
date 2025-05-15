package org.example.peluqueria.infraestructure.controllers;

import lombok.RequiredArgsConstructor;
import org.example.peluqueria.application.service.order.OrderService;
import org.example.peluqueria.domain.models.Order;
import org.example.peluqueria.infraestructure.dto.PageOutDto;
import org.example.peluqueria.infraestructure.dto.order.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/appointment/{appointmentId}")
    public ResponseEntity<OrderResponseDto> createOrder(@PathVariable Long appointmentId) {
        Order order = orderService.createOrder(appointmentId);
        OrderResponseDto response = OrderResponseDto.fromEntity(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{orderId}/pay")
    public ResponseEntity<Void> markOrderAsPaid(@PathVariable Long orderId) {
        orderService.markOrderAsPaid(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<PageOutDto<OrderResponseDto>> getOrdersByClient(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderService.getOrdersByClient(clientId, pageable);

        List<OrderResponseDto> content = orderPage.getContent()
                .stream()
                .map(OrderResponseDto::fromEntity)
                .toList();

        PageOutDto<OrderResponseDto> response = new PageOutDto<>(
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                content
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/{clientId}/{idOrder}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long clientId, @PathVariable Long idOrder) {

        return ResponseEntity.ok(OrderResponseDto.fromEntity(orderService.getOrderByAppIdAndOrderId(clientId, idOrder)));
    }

    @GetMapping
    public ResponseEntity<PageOutDto<OrderResponseDto>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderService.findAllorders(pageable);

        List<OrderResponseDto> content = orderPage.getContent()
                .stream()
                .map(OrderResponseDto::fromEntity)
                .toList();

        PageOutDto<OrderResponseDto> response = new PageOutDto<>(
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                content
        );

        return ResponseEntity.ok(response);

    }
}
