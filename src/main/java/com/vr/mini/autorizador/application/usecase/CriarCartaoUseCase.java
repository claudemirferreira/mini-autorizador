package com.vr.mini.autorizador.application.usecase;


import com.vr.mini.autorizador.domain.CartaoDomain;

public interface CriarCartaoUseCase {

    CartaoDomain criar(String numeroCartao, String senha) throws Exception;
}
