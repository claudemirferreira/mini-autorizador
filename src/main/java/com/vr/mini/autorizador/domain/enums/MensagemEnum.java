package com.vr.mini.autorizador.domain.enums;

import lombok.Getter;

@Getter
public enum MensagemEnum {
    CARTAO_EXISTENTE("Cartão já existe na base de dados");

    private final String descricao;

    MensagemEnum(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
} 