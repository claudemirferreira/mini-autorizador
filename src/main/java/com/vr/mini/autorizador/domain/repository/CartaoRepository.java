package com.vr.mini.autorizador.domain.repository;


import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.infrastructure.entity.CartaoEntity;

import java.util.Optional;

public interface CartaoRepository {
    CartaoDomain criar(CartaoDomain cartao);
    Optional<CartaoDomain> findByNumeroCartao(String numeroCartao);
}