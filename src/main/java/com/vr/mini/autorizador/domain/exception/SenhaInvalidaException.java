package com.vr.mini.autorizador.domain.exception;

import static com.vr.mini.autorizador.domain.enums.MensagemEnum.SENHA_INVALIDA;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException() {
        super(SENHA_INVALIDA.getDescricao());
    }
} 