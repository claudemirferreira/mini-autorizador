package com.vr.mini.autorizador.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarCartaoRequest {
    
    @NotNull(message = "O número do cartão é obrigatório ou deve ser informado")
    private String numeroCartao;
    
    @NotNull(message = "A senha é obrigatório ou deve ser informado")
    private String senha;
}