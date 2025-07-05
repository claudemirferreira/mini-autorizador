package com.vr.mini.autorizador.infrastructure.mapper;


import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.infrastructure.entity.CartaoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartaoMapper {
    CartaoDomain toDomain(CartaoEntity entity);
    CartaoEntity toEntity(CartaoDomain domain);
} 