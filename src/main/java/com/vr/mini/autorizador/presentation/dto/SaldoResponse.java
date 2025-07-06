package com.vr.mini.autorizador.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(
        description = "Resposta contendo informações de saldo do cartão",
        name = "SaldoResponse"
)
public record SaldoResponse(
        @Schema(
                description = "Número do cartão",
                example = "6549873025634501",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String numeroCartao,

        @Schema(
                description = "Valor do saldo disponível",
                example = "500.00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal saldo,

        @Schema(
                description = "Saldo formatado em moeda",
                example = "R$ 500,00",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        String saldoFormatado
) {
    public SaldoResponse(String numeroCartao, BigDecimal saldo) {
        this(numeroCartao, saldo, String.format("R$ %,.2f", saldo));
    }
}