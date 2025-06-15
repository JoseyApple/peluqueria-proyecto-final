package org.example.peluqueria.exceptions;

public class InvalidOrderStatusCondition extends RuntimeException {
    public InvalidOrderStatusCondition(String message) {
        super(message);
    }
}
