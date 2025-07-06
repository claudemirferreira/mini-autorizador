package com.vr.mini.autorizador.application.usecase;

import java.math.BigDecimal;

public interface DebitarUseCase {
    void execute(String numeroCartao, String senha, BigDecimal valor);
}
