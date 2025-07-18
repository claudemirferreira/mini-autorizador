package com.vr.mini.autorizador.domain.enums;

import lombok.Getter;

@Getter
public enum MensagemEnum {
    CARTAO_EXISTENTE("Cartão já existe na base de dados"),
    CARTAO_INEXISTENTE("Cartão não existe na base de dados"),
    SALDO_INSUFICIENTE("Saldo insuficiente"),
    SENHA_INVALIDA("Senha invalida"),
    CARTAO_NAO_ENCONTRADO("Cartão não eexiste na base de dados");

    private final String descricao;

    MensagemEnum(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
} 