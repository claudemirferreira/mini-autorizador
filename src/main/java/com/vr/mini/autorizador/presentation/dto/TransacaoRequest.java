package com.vr.mini.autorizador.presentation.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransacaoRequest(
        @NotNull(message = "O número do cartão é obrigatório ou deve ser informado")
        String numeroCartao,
        @NotNull(message = "A senha é obrigatório ou deve ser informado")
        String senhaCartao,
        @NotNull(message = "O valor é obrigatório ou deve ser informado")
        @DecimalMin(value = "0.01")
        BigDecimal valor

) {
} 