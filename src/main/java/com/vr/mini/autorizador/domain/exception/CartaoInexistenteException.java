package com.vr.mini.autorizador.domain.exception;

import com.vr.mini.autorizador.domain.enums.MensagemEnum;

public class CartaoInexistenteException extends RuntimeException {
    public CartaoInexistenteException() {
        super(MensagemEnum.CARTAO_INEXISTENTE.getDescricao());
    }
} 