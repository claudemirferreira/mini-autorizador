package com.vr.mini.autorizador.application.usecase.impl;

import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.domain.exception.CartaoNaoEncontradoException;
import com.vr.mini.autorizador.domain.repository.CartaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsultarSaldoCartaoUseCaseImplTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @InjectMocks
    private ConsultarSaldoCartaoUseCaseImpl consultarSaldoCartaoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar o cartão quando ele existe no repositório")
    void deveRetornarCartaoQuandoExistente() {
        String numeroCartao = "1234567890123456";
        CartaoDomain cartao = new CartaoDomain(numeroCartao, "1234", new BigDecimal("500.00"));
        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));

        CartaoDomain resultado = consultarSaldoCartaoUseCase.execute(numeroCartao);

        assertNotNull(resultado);
        assertEquals(numeroCartao, resultado.getNumeroCartao());
        assertEquals(new BigDecimal("500.00"), resultado.getSaldo());
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
    }

    @Test
    @DisplayName("Deve lançar CartaoNaoEncontradoException quando o cartão não existe")
    void deveLancarExcecaoQuandoCartaoNaoEncontrado() {
        String numeroCartao = "9999999999999999";
        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.empty());

        assertThrows(CartaoNaoEncontradoException.class, () ->
                consultarSaldoCartaoUseCase.execute(numeroCartao)
        );
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
    }
} 