package com.vr.mini.autorizador.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(
        description = "Representação do saldo e informações de um cartão",
        name = "SaldoResponse"
)
public record SaldoResponse(
        @Schema(
                description = "Número do cartão associado ao saldo",
                example = "1234567890123456",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 16,
                maxLength = 16
        )
        String numeroCartao,

        @Schema(
                description = "Valor do saldo disponível no cartão",
                example = "500.00",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = BigDecimal.class
        )
        BigDecimal saldo
) {
    @Schema(
            description = "Representação formatada do saldo",
            example = "R$ 500,00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    public String getSaldoFormatado() {
        return String.format("R$ %,.2f", saldo);
    }
}