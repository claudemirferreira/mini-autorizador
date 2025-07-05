package com.vr.mini.autorizador.domain.exception;

import com.vr.mini.autorizador.domain.enums.MensagemEnum;

public class CartaoJaExisteException extends RuntimeException {
    public CartaoJaExisteException() {
        super(MensagemEnum.CARTAO_EXISTENTE.getDescricao());
    }
} 