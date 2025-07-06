package com.vr.mini.autorizador.domain.exception;

import com.vr.mini.autorizador.domain.enums.MensagemEnum;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException() {
        super(MensagemEnum.SALDO_INSUFICIENTE.getDescricao());
    }
} 