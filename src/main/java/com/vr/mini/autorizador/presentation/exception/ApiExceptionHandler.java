package com.vr.mini.autorizador.presentation.exception;

import com.vr.mini.autorizador.domain.exception.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.LinkedHashMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    public static final String TIMESTAMP = "timestamp";
    public static final String STATUS = "status";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String ERRORS = "errors";

    @ExceptionHandler(CartaoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleCartaoNaoEncontrado(CartaoNaoEncontradoException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, OffsetDateTime.now());
        body.put(STATUS, HttpStatus.NOT_FOUND.value());
        body.put(ERROR, "CARTAO_NAO_ENCONTRADO");
        body.put(MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler({CartaoJaExisteException.class,
            SenhaInvalidaException.class,
            SaldoInsuficienteException.class})
    public ResponseEntity<Map<String, Object>> handleBusinessExceptions(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, OffsetDateTime.now());
        body.put(STATUS, HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put(ERROR, ex.getClass().getSimpleName());
        body.put(MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, OffsetDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ERROR, "VALIDATION_ERROR");
        body.put(ERRORS, List.copyOf(errors));  // Garante imutabilidade

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, OffsetDateTime.now());
        body.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put(ERROR, "INTERNAL_SERVER_ERROR");
        body.put(MESSAGE, "Ocorreu um erro inesperado");

        // Logar o erro completo para debug
        ex.printStackTrace();

        return ResponseEntity.internalServerError().body(body);
    }
}