package com.vr.mini.autorizador.presentation.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransacaoResponse(
        String mensagem,
        OffsetDateTime timestamp,
        BigDecimal valorDebitado
) {
} 