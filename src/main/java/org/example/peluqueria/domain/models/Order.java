package org.example.peluqueria.domain.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.peluqueria.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private BigDecimal totalAmount;

    @ManyToOne
    private AppUser client;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
