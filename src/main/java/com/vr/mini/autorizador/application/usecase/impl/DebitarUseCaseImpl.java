package com.vr.mini.autorizador.application.usecase.impl;

import java.math.BigDecimal;

import com.vr.mini.autorizador.application.usecase.DebitarUseCase;
import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.domain.exception.CartaoInexistenteException;
import com.vr.mini.autorizador.domain.exception.SaldoInsuficienteException;
import com.vr.mini.autorizador.domain.exception.SenhaInvalidaException;
import com.vr.mini.autorizador.domain.repository.CartaoRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebitarUseCaseImpl implements DebitarUseCase {
    private final CartaoRepository cartaoRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Transactional
    public void execute(String numeroCartao, String senha, BigDecimal valor) {
        log.info("Iniciando a transferencia da conta {} no valor {}", numeroCartao, valor);
        CartaoDomain cartao = cartaoRepository.findByNumeroCartao(numeroCartao)
                .orElseThrow(CartaoInexistenteException::new);
        validar(cartao, valor, senha);
        cartaoRepository.debitar(cartao, valor);
        log.info("Finalizou a transferencia da conta {} no valor {}", numeroCartao, valor);
    }

    public void validar(CartaoDomain cartao, BigDecimal valor, String senha) {
        validarSaldo(cartao, valor);
        validarSenha(cartao, senha);
    }

    private void validarSaldo(CartaoDomain cartao, BigDecimal valor) {
        if (cartao.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException();
        }
    }

    private void validarSenha(CartaoDomain cartao, String senha) {
        if (!passwordEncoder.matches(senha, cartao.getSenha())) {
            throw new SenhaInvalidaException();
        }
    }
}