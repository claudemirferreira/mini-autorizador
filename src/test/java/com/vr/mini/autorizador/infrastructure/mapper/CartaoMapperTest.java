package com.vr.mini.autorizador.infrastructure.mapper;

import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.infrastructure.entity.CartaoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartaoMapperTest {

    private final CartaoMapper mapper = new CartaoMapper();

    @Test
    @DisplayName("Deve mapear CartaoDomain para CartaoEntity corretamente")
    void deveMapearDomainParaEntity() {
        CartaoDomain domain = CartaoDomain.builder()
                .numeroCartao("1234567890123456")
                .saldo(new BigDecimal("100.00"))
                .senha("senha123")
                .build();

        CartaoEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getNumeroCartao(), entity.getNumeroCartao());
        assertEquals(domain.getSaldo(), entity.getSaldo());
        assertEquals(domain.getSenha(), entity.getSenha());
    }

    @Test
    @DisplayName("Deve mapear CartaoEntity para CartaoDomain corretamente")
    void deveMapearEntityParaDomain() {
        CartaoEntity entity = CartaoEntity.builder()
                .numeroCartao("1234567890123456")
                .saldo(new BigDecimal("200.00"))
                .senha("senha456")
                .build();

        CartaoDomain domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getNumeroCartao(), domain.getNumeroCartao());
        assertEquals(entity.getSaldo(), domain.getSaldo());
        assertEquals(entity.getSenha(), domain.getSenha());
    }
} 