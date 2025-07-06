package com.vr.mini.autorizador.application.usecase.impl;

import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.domain.exception.CartaoInexistenteException;
import com.vr.mini.autorizador.domain.exception.SaldoInsuficienteException;
import com.vr.mini.autorizador.domain.exception.SenhaInvalidaException;
import com.vr.mini.autorizador.domain.repository.CartaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DebitarUseCaseImplTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private DebitarUseCaseImpl debitarUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve debitar com sucesso quando dados são válidos")
    void deveDebitarComSucesso() {
        String numeroCartao = "1234567890123456";
        String senha = "1234";
        String senhaCriptografada = "senhaCriptografada";
        BigDecimal saldo = new BigDecimal("100.00");
        BigDecimal valor = new BigDecimal("50.00");
        CartaoDomain cartao = new CartaoDomain(numeroCartao, senhaCriptografada, saldo);

        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));
        when(passwordEncoder.matches(senha, senhaCriptografada)).thenReturn(true);

        assertDoesNotThrow(() -> debitarUseCase.execute(numeroCartao, senha, valor));
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
        verify(passwordEncoder, times(1)).matches(senha, senhaCriptografada);
    }

    @Test
    @DisplayName("Deve lançar CartaoInexistenteException quando o cartão não existe")
    void deveLancarExcecaoQuandoCartaoInexistente() {
        String numeroCartao = "9999999999999999";
        String senha = "1234";
        BigDecimal valor = new BigDecimal("10.00");
        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.empty());

        assertThrows(CartaoInexistenteException.class, () ->
                debitarUseCase.execute(numeroCartao, senha, valor)
        );
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar SaldoInsuficienteException quando saldo é insuficiente")
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        String numeroCartao = "1234567890123456";
        String senha = "1234";
        String senhaCriptografada = "senhaCriptografada";
        BigDecimal saldo = new BigDecimal("10.00");
        BigDecimal valor = new BigDecimal("50.00");
        CartaoDomain cartao = new CartaoDomain(numeroCartao, senhaCriptografada, saldo);

        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));

        assertThrows(SaldoInsuficienteException.class, () ->
                debitarUseCase.execute(numeroCartao, senha, valor)
        );
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar SenhaInvalidaException quando a senha está incorreta")
    void deveLancarExcecaoQuandoSenhaInvalida() {
        String numeroCartao = "1234567890123456";
        String senha = "senhaErrada";
        String senhaCriptografada = "senhaCriptografada";
        BigDecimal saldo = new BigDecimal("100.00");
        BigDecimal valor = new BigDecimal("50.00");
        CartaoDomain cartao = new CartaoDomain(numeroCartao, senhaCriptografada, saldo);

        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));
        when(passwordEncoder.matches(senha, senhaCriptografada)).thenReturn(false);

        assertThrows(SenhaInvalidaException.class, () ->
                debitarUseCase.execute(numeroCartao, senha, valor)
        );
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
        verify(passwordEncoder, times(1)).matches(senha, senhaCriptografada);
    }
} 