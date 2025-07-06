package com.vr.mini.autorizador.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import org.hamcrest.Matchers;
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
import com.vr.mini.autorizador.application.usecase.ConsultarSaldoCartaoUseCase;
import com.vr.mini.autorizador.application.usecase.CriarCartaoUseCase;
import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.domain.exception.CartaoJaExisteException;
import com.vr.mini.autorizador.domain.exception.CartaoNaoEncontradoException;
import com.vr.mini.autorizador.presentation.dto.CriarCartaoRequest;
import com.vr.mini.autorizador.presentation.exception.ApiExceptionHandler;

@ExtendWith(MockitoExtension.class)
@DisplayName("CartaoController - Testes Unitários")
class CartaoControllerTest {

    @Mock
    private CriarCartaoUseCase criarCartaoUseCase;
    @Mock
    private ConsultarSaldoCartaoUseCase consultarSaldoCartaoUseCase;
    @InjectMocks
    private CartaoController cartaoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartaoController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Deve criar cartão com sucesso")
    void deveCriarCartaoComSucesso() throws Exception {
        CriarCartaoRequest request = new CriarCartaoRequest("6549873025634501", "1234");
        CartaoDomain cartaoDomain = CartaoDomain.criarCartao("6549873025634501", "1234");
        when(criarCartaoUseCase.execute(eq("6549873025634501"), eq("1234"))).thenReturn(cartaoDomain);

        mockMvc.perform(post("/cartoes/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCartao").value("6549873025634501"))
                .andExpect(jsonPath("$.saldo").value(Matchers.anyOf(
                        Matchers.is(500.00),
                        Matchers.is(500)
                )))
                .andExpect(jsonPath("$.saldoFormatado").value(Matchers.anyOf(
                        Matchers.is("R$ 500,00"),
                        Matchers.is("R$ 500.00")
                )));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando campos obrigatórios estão ausentes na criação")
    void deveRetornarErro400QuandoCamposObrigatoriosAusentes() throws Exception {
        String jsonSemSenha = "{" +
                "\"numeroCartao\": \"6549873025634501\"" +
                "}";
        mockMvc.perform(post("/cartoes/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSemSenha))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("Deve retornar erro 422 quando cartão já existe")
    void deveRetornarErro422QuandoCartaoJaExiste() throws Exception {
        CriarCartaoRequest request = new CriarCartaoRequest("6549873025634501", "1234");
        doThrow(new CartaoJaExisteException()).when(criarCartaoUseCase).execute(any(), any());

        mockMvc.perform(post("/cartoes/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("CartaoJaExisteException"));
    }

    @Test
    @DisplayName("Deve consultar saldo com sucesso")
    void deveConsultarSaldoComSucesso() throws Exception {
        CartaoDomain cartaoDomain = CartaoDomain.builder()
            .numeroCartao("6549873025634501")
            .senha("1234")
            .saldo(new BigDecimal("500.00"))
            .build();
        when(consultarSaldoCartaoUseCase.execute(eq("6549873025634501"))).thenReturn(cartaoDomain);

        try {
            mockMvc.perform(get("/cartoes/6549873025634501"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numeroCartao").value("6549873025634501"))
                    .andExpect(jsonPath("$.saldo").value(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.is(500.00),
                        org.hamcrest.Matchers.is(500)
                    )))
                    .andExpect(jsonPath("$.saldoFormatado").value(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.is("R$ 500,00"),
                        org.hamcrest.Matchers.is("R$ 500.00")
                    )));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Deve retornar erro 404 ao consultar saldo de cartão inexistente")
    void deveRetornarErro404QuandoCartaoNaoEncontrado() throws Exception {
        when(consultarSaldoCartaoUseCase.execute(eq("9999999999999999")))
                .thenThrow(new CartaoNaoEncontradoException("9999999999999999"));

        mockMvc.perform(get("/cartoes/9999999999999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("CARTAO_NAO_ENCONTRADO"));
    }
} 