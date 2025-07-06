package com.vr.mini.autorizador.domain.exception;

import com.vr.mini.autorizador.domain.enums.MensagemEnum;

public class CartaoNaoEncontradoException extends RuntimeException {

    public CartaoNaoEncontradoException(String msg) {
        super(MensagemEnum.CARTAO_NAO_ENCONTRADO.getDescricao() + " " + msg);
    }
} 