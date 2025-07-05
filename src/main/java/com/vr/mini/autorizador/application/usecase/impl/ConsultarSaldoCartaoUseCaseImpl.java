package com.vr.mini.autorizador.application.usecase.impl;

import com.vr.mini.autorizador.application.usecase.ConsultarSaldoCartaoUseCase;
import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.domain.exception.CartaoNaoEncontradoException;
import com.vr.mini.autorizador.domain.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultarSaldoCartaoUseCaseImpl implements ConsultarSaldoCartaoUseCase {

    private final CartaoRepository cartaoRepository;

    @Override
    public CartaoDomain execute(String numeroCartao) {
        log.info("Consultando saldo para o cartão: {}", numeroCartao);

        CartaoDomain cartao = cartaoRepository.findByNumeroCartao(numeroCartao)
                .orElseThrow(() -> {
                    log.error("Cartão não encontrado: {}", numeroCartao);
                    return new CartaoNaoEncontradoException(numeroCartao);
                });

        log.info("Saldo encontrado para cartão {}: R$ {}", numeroCartao, cartao.getSaldo());
        return cartao;
    }
}