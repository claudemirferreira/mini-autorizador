package com.vr.mini.autorizador.infrastructure.mapper;

import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.infrastructure.entity.CartaoEntity;
import org.springframework.stereotype.Service;

@Service
public class CartaoMapper {

    public CartaoEntity toEntity(CartaoDomain cartaoDomain){
        return CartaoEntity
                .builder()
                .numeroCartao(cartaoDomain.getNumeroCartao())
                .saldo(cartaoDomain.getSaldo())
                .senha(cartaoDomain.getSenha())
                .build();
    }

    public CartaoDomain toDomain(CartaoEntity cartaoEntity){
        return CartaoDomain
                .builder()
                .numeroCartao(cartaoEntity.getNumeroCartao())
                .saldo(cartaoEntity.getSaldo())
                .senha(cartaoEntity.getSenha())
                .build();
    }
}
