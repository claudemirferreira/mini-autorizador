package com.vr.mini.autorizador.domain.repository;


import com.vr.mini.autorizador.domain.CartaoDomain;

import java.math.BigDecimal;
import java.util.Optional;

public interface CartaoRepository {
    CartaoDomain criar(CartaoDomain cartao);
    Optional<CartaoDomain> findByNumeroCartao(String numeroCartao);
    CartaoDomain debitar(CartaoDomain cartao, BigDecimal valor);
}