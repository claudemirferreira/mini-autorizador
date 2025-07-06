package com.vr.mini.autorizador.presentation.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.vr.mini.autorizador.domain.exception.CartaoJaExisteException;
import com.vr.mini.autorizador.domain.exception.CartaoNaoEncontradoException;
import com.vr.mini.autorizador.domain.exception.SaldoInsuficienteException;
import com.vr.mini.autorizador.domain.exception.SenhaInvalidaException;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    @DisplayName("Deve tratar CartaoNaoEncontradoException retornando NOT_FOUND")
    void deveTratarCartaoNaoEncontradoException() {
        CartaoNaoEncontradoException ex = new CartaoNaoEncontradoException("123");
        ResponseEntity<Map<String, Object>> response = handler.handleCartaoNaoEncontrado(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("CARTAO_NAO_ENCONTRADO", response.getBody().get("error"));
        assertEquals(ex.getMessage(), response.getBody().get("message"));
    }

    @Test
    @DisplayName("Deve tratar CartaoJaExisteException retornando UNPROCESSABLE_ENTITY")
    void deveTratarCartaoJaExisteException() {
        CartaoJaExisteException ex = new CartaoJaExisteException();
        ResponseEntity<Map<String, Object>> response = handler.handleBusinessExceptions(ex);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("CartaoJaExisteException", response.getBody().get("error"));
        assertEquals(ex.getMessage(), response.getBody().get("message"));
    }

    @Test
    @DisplayName("Deve tratar SenhaInvalidaException retornando UNPROCESSABLE_ENTITY")
    void deveTratarSenhaInvalidaException() {
        SenhaInvalidaException ex = new SenhaInvalidaException();
        ResponseEntity<Map<String, Object>> response = handler.handleBusinessExceptions(ex);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("SenhaInvalidaException", response.getBody().get("error"));
        assertEquals(ex.getMessage(), response.getBody().get("message"));
    }

    @Test
    @DisplayName("Deve tratar SaldoInsuficienteException retornando UNPROCESSABLE_ENTITY")
    void deveTratarSaldoInsuficienteException() {
        SaldoInsuficienteException ex = new SaldoInsuficienteException();
        ResponseEntity<Map<String, Object>> response = handler.handleBusinessExceptions(ex);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("SaldoInsuficienteException", response.getBody().get("error"));
        assertEquals(ex.getMessage(), response.getBody().get("message"));
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException retornando BAD_REQUEST e lista de erros")
    void deveTratarValidationException() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("obj", "campo", "mensagem de erro");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidationExceptions(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("VALIDATION_ERROR", response.getBody().get("error"));
        List<String> errors = (List<String>) response.getBody().get("errors");
        assertTrue(errors.contains("mensagem de erro"));
    }

    @Test
    @DisplayName("Deve tratar Exception genérica retornando INTERNAL_SERVER_ERROR")
    void deveTratarExceptionGenerica() {
        Exception ex = new Exception("Erro inesperado");
        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().get("error"));
        assertEquals("Ocorreu um erro inesperado", response.getBody().get("message"));
    }
} 