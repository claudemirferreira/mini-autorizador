package com.vr.mini.autorizador.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TransacaoResponse {
    private String mensagem;
    private OffsetDateTime timestamp;
    private BigDecimal valorDebitado;
} 