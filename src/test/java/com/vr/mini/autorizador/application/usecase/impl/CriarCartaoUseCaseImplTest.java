package com.vr.mini.autorizador.application.usecase.impl;

import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.domain.exception.CartaoJaExisteException;
import com.vr.mini.autorizador.domain.repository.CartaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriarCartaoUseCaseImplTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private CriarCartaoUseCaseImpl criarCartaoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar um cartão quando ele não existe")
    void deveCriarCartaoQuandoNaoExiste() {
        String numeroCartao = "1234567890123456";
        String senha = "1234";
        String senhaCriptografada = "senhaCriptografada";

        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(senha)).thenReturn(senhaCriptografada);

        CartaoDomain cartao = criarCartaoUseCase.execute(numeroCartao, senha);

        assertNotNull(cartao);
        assertEquals(numeroCartao, cartao.getNumeroCartao());
        assertNotNull(cartao.getSenha());
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
        verify(passwordEncoder, times(1)).encode(senha);
    }

    @Test
    @DisplayName("Deve lançar CartaoJaExisteException quando o cartão já existe")
    void deveLancarExcecaoQuandoCartaoJaExiste() {
        String numeroCartao = "1234567890123456";
        String senha = "1234";
        CartaoDomain cartaoExistente = mock(CartaoDomain.class);
        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartaoExistente));

        assertThrows(CartaoJaExisteException.class, () ->
                criarCartaoUseCase.execute(numeroCartao, senha)
        );
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
        verify(passwordEncoder, never()).encode(anyString());
    }
} 