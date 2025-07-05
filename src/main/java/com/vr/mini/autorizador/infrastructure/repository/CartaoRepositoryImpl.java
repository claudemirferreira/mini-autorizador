package com.vr.mini.autorizador.infrastructure.repository;

import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.domain.repository.CartaoRepository;
import com.vr.mini.autorizador.infrastructure.entity.CartaoEntity;
import com.vr.mini.autorizador.infrastructure.mapper.CartaoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Slf4j
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
    public Optional<CartaoDomain> findByNumeroCartao(String numeroCartao) {
        log.debug("Buscando cartão por número: {}", numeroCartao);
        return jpaRepository.findByNumeroCartao(numeroCartao)
                .map(entity -> {
                    log.debug("Cartão encontrado: {}", numeroCartao);
                    return mapper.toDomain(entity);
                });
    }

}