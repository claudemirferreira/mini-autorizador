package com.vr.mini.autorizador.infrastructure.repository;

import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.domain.repository.CartaoRepository;
import com.vr.mini.autorizador.infrastructure.entity.CartaoEntity;
import com.vr.mini.autorizador.infrastructure.mapper.CartaoMapper;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartaoRepositoryImpl implements CartaoRepository {
    private final CartaoJpaRepository jpaRepository;
    private final CartaoMapper mapper;

    @Override
    public CartaoDomain criar(CartaoDomain cartao) {
        CartaoEntity entity = mapper.toEntity(cartao);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<CartaoEntity> findByNumeroCartao(String numeroCartao) {
        return jpaRepository.findByNumeroCartao(numeroCartao);
    }

}