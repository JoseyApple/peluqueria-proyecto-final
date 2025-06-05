package org.example.peluqueria.infraestructure.controllers;

import lombok.RequiredArgsConstructor;
import org.example.peluqueria.application.service.order.OrderService;
import org.example.peluqueria.domain.OrderStatus;
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

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam("newStatus") OrderStatus newStatus) {

        orderService.changeOrderStatus(id, newStatus);
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
