package com.vr.mini.autorizador.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MensagemEnumTest {

    @Test
    @DisplayName("toString deve retornar a descrição correta para cada enum")
    void testToString() {
        assertEquals("Cartão já existe na base de dados", MensagemEnum.CARTAO_EXISTENTE.toString());
        assertEquals("Cartão não existe na base de dados", MensagemEnum.CARTAO_INEXISTENTE.toString());
        assertEquals("Saldo insuficiente", MensagemEnum.SALDO_INSUFICIENTE.toString());
        assertEquals("Senha invalida", MensagemEnum.SENHA_INVALIDA.toString());
        assertEquals("Cartão não eexiste na base de dados", MensagemEnum.CARTAO_NAO_ENCONTRADO.toString());
    }
}
