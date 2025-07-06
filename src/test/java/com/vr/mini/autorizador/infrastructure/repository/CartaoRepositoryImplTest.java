package com.vr.mini.autorizador.infrastructure.repository;

import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.domain.exception.CartaoNaoEncontradoException;
import com.vr.mini.autorizador.domain.exception.SaldoInsuficienteException;
import com.vr.mini.autorizador.infrastructure.entity.CartaoEntity;
import com.vr.mini.autorizador.infrastructure.mapper.CartaoMapper;
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

class CartaoRepositoryImplTest {

    @Mock
    private CartaoJpaRepository cartaoJpaRepository;

    @Mock
    private CartaoMapper cartaoMapper;

    @InjectMocks
    private CartaoRepositoryImpl cartaoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar e retornar o cartão corretamente")
    void deveCriarCartao() {
        CartaoDomain domain = new CartaoDomain("123", "senha", new BigDecimal("100.00"));
        CartaoEntity entity = new CartaoEntity();
        CartaoEntity savedEntity = new CartaoEntity();
        CartaoDomain domainRetornado = new CartaoDomain("123", "senha", new BigDecimal("100.00"));

        when(cartaoMapper.toEntity(domain)).thenReturn(entity);
        when(cartaoJpaRepository.save(entity)).thenReturn(savedEntity);
        when(cartaoMapper.toDomain(savedEntity)).thenReturn(domainRetornado);

        CartaoDomain result = cartaoRepository.criar(domain);
        assertNotNull(result);
        assertEquals(domainRetornado, result);
        verify(cartaoMapper).toEntity(domain);
        verify(cartaoJpaRepository).save(entity);
        verify(cartaoMapper).toDomain(savedEntity);
    }

    @Test
    @DisplayName("Deve retornar cartão quando encontrado por número")
    void deveRetornarCartaoQuandoEncontrado() {
        String numero = "123";
        CartaoEntity entity = new CartaoEntity();
        CartaoDomain domain = new CartaoDomain(numero, "senha", new BigDecimal("100.00"));
        when(cartaoJpaRepository.findByNumeroCartao(numero)).thenReturn(Optional.of(entity));
        when(cartaoMapper.toDomain(entity)).thenReturn(domain);

        Optional<CartaoDomain> result = cartaoRepository.findByNumeroCartao(numero);
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
        verify(cartaoJpaRepository).findByNumeroCartao(numero);
        verify(cartaoMapper).toDomain(entity);
    }

    @Test
    @DisplayName("Deve retornar vazio quando cartão não encontrado por número")
    void deveRetornarVazioQuandoCartaoNaoEncontrado() {
        String numero = "123";
        when(cartaoJpaRepository.findByNumeroCartao(numero)).thenReturn(Optional.empty());

        Optional<CartaoDomain> result = cartaoRepository.findByNumeroCartao(numero);
        assertFalse(result.isPresent());
        verify(cartaoJpaRepository).findByNumeroCartao(numero);
        verify(cartaoMapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Deve debitar corretamente quando saldo suficiente")
    void deveDebitarComSaldoSuficiente() {
        String numero = "123";
        BigDecimal saldo = new BigDecimal("100.00");
        BigDecimal valor = new BigDecimal("50.00");
        CartaoDomain domain = new CartaoDomain(numero, "senha", saldo);
        CartaoEntity entity = new CartaoEntity();
        entity.setNumeroCartao(numero);
        entity.setSaldo(saldo);
        CartaoDomain domainRetornado = new CartaoDomain(numero, "senha", saldo.subtract(valor));

        when(cartaoJpaRepository.findByNumeroCartaoForUpdate(numero)).thenReturn(Optional.of(entity));
        // Não mocka validarSaldo pois é private e testamos o fluxo normal
        when(cartaoMapper.toDomain(entity)).thenReturn(domainRetornado);

        CartaoDomain result = cartaoRepository.debitar(domain, valor);
        assertNotNull(result);
        assertEquals(domainRetornado, result);
        assertEquals(new BigDecimal("50.00"), entity.getSaldo());
        verify(cartaoJpaRepository).findByNumeroCartaoForUpdate(numero);
        verify(cartaoMapper).toDomain(entity);
    }

    @Test
    @DisplayName("Deve lançar CartaoNaoEncontradoException ao debitar cartão inexistente")
    void deveLancarExcecaoQuandoCartaoNaoEncontradoAoDebitar() {
        String numero = "123";
        BigDecimal valor = new BigDecimal("10.00");
        CartaoDomain domain = new CartaoDomain(numero, "senha", new BigDecimal("100.00"));
        when(cartaoJpaRepository.findByNumeroCartaoForUpdate(numero)).thenReturn(Optional.empty());

        assertThrows(CartaoNaoEncontradoException.class, () ->
                cartaoRepository.debitar(domain, valor)
        );
        verify(cartaoJpaRepository).findByNumeroCartaoForUpdate(numero);
    }

    @Test
    @DisplayName("Deve lançar SaldoInsuficienteException ao debitar valor maior que saldo")
    void deveLancarExcecaoQuandoSaldoInsuficienteAoDebitar() {
        String numero = "123";
        BigDecimal saldo = new BigDecimal("10.00");
        BigDecimal valor = new BigDecimal("50.00");
        CartaoDomain domain = new CartaoDomain(numero, "senha", saldo);
        CartaoEntity entity = new CartaoEntity();
        entity.setNumeroCartao(numero);
        entity.setSaldo(saldo);
        when(cartaoJpaRepository.findByNumeroCartaoForUpdate(numero)).thenReturn(Optional.of(entity));

        assertThrows(SaldoInsuficienteException.class, () ->
                cartaoRepository.debitar(domain, valor)
        );
        verify(cartaoJpaRepository).findByNumeroCartaoForUpdate(numero);
    }
} 