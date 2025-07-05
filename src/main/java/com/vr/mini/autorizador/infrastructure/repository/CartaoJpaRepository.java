package com.vr.mini.autorizador.infrastructure.repository;

import com.vr.mini.autorizador.infrastructure.entity.CartaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartaoJpaRepository extends JpaRepository<CartaoEntity, String> {
    Optional<CartaoEntity> findByNumeroCartao(String numeroCartao);
}
