package com.vr.mini.autorizador.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.mini.autorizador.application.usecase.DebitarUseCase;
import com.vr.mini.autorizador.domain.exception.CartaoNaoEncontradoException;
import com.vr.mini.autorizador.domain.exception.SaldoInsuficienteException;
import com.vr.mini.autorizador.domain.exception.SenhaInvalidaException;
import com.vr.mini.autorizador.presentation.dto.TransacaoRequest;
import com.vr.mini.autorizador.presentation.exception.ApiExceptionHandler;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransacaoController - Testes Unitários")
class TransacaoControllerTest {

    @Mock
    private DebitarUseCase debitarUseCase;

    @InjectMocks
    private TransacaoController transacaoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transacaoController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Deve realizar transação com sucesso quando dados são válidos")
    void deveRealizarTransacaoComSucesso() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                "123456",
                new BigDecimal("150.00")
        );

        doNothing().when(debitarUseCase).execute(
                eq("6549873025634501"),
                eq("123456"),
                eq(new BigDecimal("150.00"))
        );

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem").value("Operação realizada com sucesso"))
                .andExpect(jsonPath("$.valorDebitado").value(150.00))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando número do cartão é nulo")
    void deveRetornarErro400QuandoNumeroCartaoNulo() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                null,
                "123456",
                new BigDecimal("150.00")
        );

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando senha é nula")
    void deveRetornarErro400QuandoSenhaNula() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                null,
                new BigDecimal("150.00")
        );

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando valor é nulo")
    void deveRetornarErro400QuandoValorNulo() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                "123456",
                null
        );

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando valor é zero")
    void deveRetornarErro400QuandoValorZero() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                "123456",
                BigDecimal.ZERO
        );

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando valor é negativo")
    void deveRetornarErro400QuandoValorNegativo() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                "123456",
                new BigDecimal("-150.00")
        );

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando valor é menor que 0.01")
    void deveRetornarErro400QuandoValorMenorQueMinimo() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                "123456",
                new BigDecimal("0.005")
        );

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve processar transação com valor decimal")
    void deveProcessarTransacaoComValorDecimal() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                "123456",
                new BigDecimal("150.75")
        );

        doNothing().when(debitarUseCase).execute(
                eq("6549873025634501"),
                eq("123456"),
                eq(new BigDecimal("150.75"))
        );

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valorDebitado").value(150.75));
    }

    @Test
    @DisplayName("Deve processar transação com valor mínimo válido")
    void deveProcessarTransacaoComValorMinimo() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                "123456",
                new BigDecimal("0.01")
        );

        doNothing().when(debitarUseCase).execute(
                eq("6549873025634501"),
                eq("123456"),
                eq(new BigDecimal("0.01"))
        );

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valorDebitado").value(0.01));
    }

    @Test
    @DisplayName("Deve processar transação com valor alto")
    void deveProcessarTransacaoComValorAlto() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                "123456",
                new BigDecimal("999999.99")
        );

        doNothing().when(debitarUseCase).execute(
                eq("6549873025634501"),
                eq("123456"),
                eq(new BigDecimal("999999.99"))
        );

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valorDebitado").value(999999.99));
    }


    @Test
    @DisplayName("Deve retornar erro 422 quando cartão não é encontrado")
    void deveRetornarErro422QuandoCartaoNaoEncontrado() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                "123456",
                new BigDecimal("150.00")
        );

        doThrow(new CartaoNaoEncontradoException("6549873025634501"))
                .when(debitarUseCase).execute(any(), any(), any());

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("CARTAO_NAO_ENCONTRADO"))
                .andExpect(jsonPath("$.message").value("Cartão não eexiste na base de dados 6549873025634501"));
    }

    @Test
    @DisplayName("Deve retornar erro 422 quando saldo é insuficiente")
    void deveRetornarErro422QuandoSaldoInsuficiente() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                "123456",
                new BigDecimal("150.00")
        );

        doThrow(new SaldoInsuficienteException())
                .when(debitarUseCase).execute(any(), any(), any());

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("SaldoInsuficienteException"))
                .andExpect(jsonPath("$.message").value("Saldo insuficiente"));
    }

    @Test
    @DisplayName("Deve retornar erro 422 quando senha é inválida")
    void deveRetornarErro422QuandoSenhaInvalida() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                "123456",
                new BigDecimal("150.00")
        );

        doThrow(new SenhaInvalidaException())
                .when(debitarUseCase).execute(any(), any(), any());

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("SenhaInvalidaException"))
                .andExpect(jsonPath("$.message").value("Senha invalida"));
    }

    @Test
    @DisplayName("Deve retornar erro 500 quando ocorre exceção genérica")
    void deveRetornarErro500QuandoExcecaoGenerica() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                "6549873025634501",
                "123456",
                new BigDecimal("150.00")
        );

        doThrow(new RuntimeException("Erro interno"))
                .when(debitarUseCase).execute(any(), any(), any());

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("Ocorreu um erro inesperado"));
    }

    @Test
    @DisplayName("Deve validar mensagens de erro de validação")
    void deveValidarMensagensErroValidacao() throws Exception {
        // Arrange
        TransacaoRequest request = new TransacaoRequest(
                null,
                null,
                null
        );

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando senhaCartao é omitido do JSON")
    void deveRetornarErro400QuandoSenhaOmitida() throws Exception {
        // Arrange: senhaCartao não está presente no JSON
        String jsonSemSenha = "{" +
                "\"numeroCartao\": \"6549873025634501\"," +
                "\"valor\": 10.00" +
                "}";

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSemSenha))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("A senha é obrigatório ou deve ser informado"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando numeroCartao é omitido do JSON")
    void deveRetornarErro400QuandoNumeroCartaoOmitido() throws Exception {
        // Arrange: numeroCartao não está presente no JSON
        String jsonSemNumero = "{" +
                "\"senhaCartao\": \"123456\"," +
                "\"valor\": 10.00" +
                "}";

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSemNumero))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("O número do cartão é obrigatório ou deve ser informado"));
    }
} 