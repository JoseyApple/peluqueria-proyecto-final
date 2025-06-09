package org.example.peluqueria.infraestructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.peluqueria.application.service.order.OrderService;
import org.example.peluqueria.domain.OrderStatus;
import org.example.peluqueria.domain.models.Order;
import org.example.peluqueria.infraestructure.dto.PageOutDto;
import org.example.peluqueria.infraestructure.dto.order.OrderResponseDto;
import org.example.peluqueria.infraestructure.security.UserPrincipal;
import org.example.peluqueria.infraestructure.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "FACTURAS", description = "Operaciones relacionadas con facturas")
public class OrderController {

    private final OrderService orderService;
    private final SecurityUtils securityUtils;

    @PatchMapping("/{id}")
    @Operation(
            summary = "Actualizar estado de una factura",
            description = "Permite cambiar el estado de una orden existente mediante su ID. Se debe indicar el nuevo estado como parámetro."
    )
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam("newStatus") String newStatus) {

        orderService.changeOrderStatus(id, newStatus);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    @Operation(
            summary = "Obtener facturas por cliente",
            description = "Devuelve una lista paginada de facturas realizadas por un cliente específico."
    )
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
    @Operation(
            summary = "Obtener factura específica por cliente",
            description = "Permite obtener una orden concreta asociada a un cliente, validando que pertenezca a dicho cliente."
    )
    public ResponseEntity<OrderResponseDto> getOrder(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long clientId,
            @PathVariable Long idOrder) {

        if (!clientId.equals(principal.getId())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Order order = orderService.findById(idOrder);

        if (!order.getClient().getId().equals(principal.getId())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(OrderResponseDto.fromEntity(orderService.getOrderByAppIdAndOrderId(clientId, order.getId())));
    }

    @GetMapping
    @Operation(
            summary = "Listar todas las facturas",
            description = "Permite obtener una lista paginada de todas las facturas disponibles en el sistema."
    )
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
