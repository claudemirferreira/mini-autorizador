package com.vr.mini.autorizador.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartaoDomain {
    private String numeroCartao;
    private String senha;
    private BigDecimal saldo;
    private static final BigDecimal SALDO_INICIAL = BigDecimal.valueOf(500.00);

    public static CartaoDomain criarCartao(String numeroCartao, String senha) {
        return CartaoDomain.builder()
                .numeroCartao(numeroCartao)
                .senha(senha)
                .saldo(SALDO_INICIAL)
                .build();
    }

}