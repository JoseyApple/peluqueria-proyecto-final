package org.example.peluqueria.infraestructure.controllers;

import org.example.peluqueria.exceptions.*;
import org.example.peluqueria.infraestructure.dto.ErrorOutDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.IllegalArgumentException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = {
            InvalidTokenException.class,
            AppUserException.class,
            UserUnauthorizedException.class
    })
    protected ResponseEntity<ErrorOutDto> handleBadCredentialsException(Exception ex) {

        ErrorOutDto error = ErrorOutDto.builder()
                .error(ex.getMessage())
                .status(401)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {
            UnprocessableEntityException.class,
            EmailNotValidException.class,
            InvalidPasswordException.class,
            InvalidOrderStatusCondition.class,
            InvalidAppointmentStatusException.class
    })
    protected ResponseEntity<ErrorOutDto> handleInvalidParameters(Exception ex) {

        ErrorOutDto error = ErrorOutDto.builder()
                .error(ex.getMessage())
                .status(422)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {
            EmailAlreadyExistsException.class,
            EntityExistsException.class
    })
    protected ResponseEntity<ErrorOutDto> handleEmailAlreadyExistsException(Exception ex) {

        ErrorOutDto error = ErrorOutDto.builder()
                .error(ex.getMessage())
                .status(409)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            UserNotFoundException.class,
            EntityNotFoundException.class
    })
    protected ResponseEntity<ErrorOutDto> handleNotFoundEntities(Exception ex) {

        ErrorOutDto error = ErrorOutDto.builder()
                .error(ex.getMessage())
                .status(404)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorOutDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        List<String> errors = result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ErrorOutDto error = ErrorOutDto.builder()
                .errors(Collections.singletonList(String.join(", ", errors)))
                .status(422)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value ={
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    protected ResponseEntity<ErrorOutDto> handleValidationExceptions(Exception ex) {

        ErrorOutDto error = ErrorOutDto.builder()
                .error(ex.getMessage())
                .status(400)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
