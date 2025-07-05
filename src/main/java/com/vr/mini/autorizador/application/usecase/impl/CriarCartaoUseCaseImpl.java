package com.vr.mini.autorizador.application.usecase.impl;

import com.vr.mini.autorizador.application.usecase.CriarCartaoUseCase;
import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.domain.exception.CartaoJaExisteException;
import com.vr.mini.autorizador.domain.repository.CartaoRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CriarCartaoUseCaseImpl implements CriarCartaoUseCase {
    private final CartaoRepository cartaoRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CartaoDomain criar(String numeroCartao, String senha) throws Exception {
        log.info("Iniciando a criacao do cartao {}", numeroCartao);
        existsByNumeroCartao(numeroCartao);
        CartaoDomain cartao =  CartaoDomain.criarCartao(passwordEncoder.encode(senha), numeroCartao);
        cartao = cartaoRepository.criar(cartao);
        log.info("finalizou a criacao do cartao {}", numeroCartao);
        return cartao;
    }

    private void existsByNumeroCartao(String numeroCartao) throws Exception{
        if (cartaoRepository.findByNumeroCartao(numeroCartao).isEmpty()) {
            throw new CartaoJaExisteException();
        }
    }
} 