package com.vr.mini.autorizador.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record CriarCartaoRequest(
        @NotNull(message = "O número do cartão é obrigatório ou deve ser informado")
        String numeroCartao,

        @NotNull(message = "A senha é obrigatório ou deve ser informado")
        String senha
) {
}