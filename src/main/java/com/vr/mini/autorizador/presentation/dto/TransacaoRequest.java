package com.vr.mini.autorizador.presentation.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransacaoRequest {

    @NotNull(message = "O número do cartão é obrigatório ou deve ser informado")
    private String numeroCartao;
    @NotNull(message = "A senha é obrigatório ou deve ser informado")
    private String senhaCartao;
    @NotNull(message = "O valor é obrigatório ou deve ser informado")
    @DecimalMin(value = "0.01")
    private BigDecimal valor;
} 