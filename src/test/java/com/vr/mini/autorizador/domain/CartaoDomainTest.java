package com.vr.mini.autorizador.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartaoDomainTest {

    @Test
    @DisplayName("Deve criar cartão com saldo inicial corretamente pelo método criarCartao")
    void deveCriarCartaoComSaldoInicial() {
        String numero = "1234567890123456";
        String senha = "senha123";
        CartaoDomain cartao = CartaoDomain.criarCartao(numero, senha);
        assertEquals(numero, cartao.getNumeroCartao());
        assertEquals(senha, cartao.getSenha());
        assertEquals(new BigDecimal("500.0"), cartao.getSaldo());
    }

    @Test
    @DisplayName("Deve criar cartão usando o builder")
    void deveCriarCartaoComBuilder() {
        CartaoDomain cartao = CartaoDomain.builder()
                .numeroCartao("1111")
                .senha("abc")
                .saldo(new BigDecimal("100.00"))
                .build();
        assertEquals("1111", cartao.getNumeroCartao());
        assertEquals("abc", cartao.getSenha());
        assertEquals(new BigDecimal("100.00"), cartao.getSaldo());
    }

    @Test
    @DisplayName("Deve testar equals e hashCode gerados pelo Lombok")
    void deveTestarEqualsEHashCode() {
        CartaoDomain c1 = CartaoDomain.builder().numeroCartao("1").senha("a").saldo(BigDecimal.TEN).build();
        CartaoDomain c2 = CartaoDomain.builder().numeroCartao("1").senha("a").saldo(BigDecimal.TEN).build();
        CartaoDomain c3 = CartaoDomain.builder().numeroCartao("2").senha("b").saldo(BigDecimal.ONE).build();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
    }

    @Test
    @DisplayName("Deve testar toString gerado pelo Lombok")
    void deveTestarToString() {
        CartaoDomain cartao = CartaoDomain.builder().numeroCartao("1").senha("a").saldo(BigDecimal.TEN).build();
        String str = cartao.toString();
        assertTrue(str.contains("numeroCartao=1"));
        assertTrue(str.contains("senha=a"));
        assertTrue(str.contains("saldo=10"));
    }
}