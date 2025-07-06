package com.vr.mini.autorizador.infrastructure.repository;

import com.vr.mini.autorizador.infrastructure.entity.CartaoEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartaoJpaRepository extends JpaRepository<CartaoEntity, String> {
    Optional<CartaoEntity> findByNumeroCartao(String numeroCartao);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "javax.persistence.lock.timeout", value = "5000") // 5 segundos
    })
    @Query("SELECT c FROM CartaoEntity c WHERE c.numeroCartao = :numeroCartao")
    Optional<CartaoEntity> debitar(@Param("numeroCartao") String numeroCartao);
}
