package com.vr.mini.autorizador.application.usecase;

import com.vr.mini.autorizador.domain.CartaoDomain;


public interface ConsultarSaldoCartaoUseCase {
    CartaoDomain execute(String numeroCartao);
}
