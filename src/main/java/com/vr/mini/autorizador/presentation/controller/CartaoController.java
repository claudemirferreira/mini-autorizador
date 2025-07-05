package com.vr.mini.autorizador.presentation.controller;

import java.util.Map;

import com.vr.mini.autorizador.application.usecase.CriarCartaoUseCase;
import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.presentation.dto.CriarCartaoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
@Tag(name = "Cartões", description = "Operações relacionadas a cartões do Mini Autorizador")
public class CartaoController {
    private final CriarCartaoUseCase criarCartaoUseCase;

    @Operation(
            summary = "Cria um novo cartão",
            description = "Endpoint para criação de um novo cartão com saldo inicial padrão de R$ 500,00.",
            tags = { "Cartões" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Cartão criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(
                                    value = "{\"numeroCartao\": \"1234567890123456\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Cartão já existe no sistema",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"mensagem\": \"Cartão já existe\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"erro\": \"Número do cartão deve ter 16 dígitos\"}"
                            )
                    )
            )
    })
    @PostMapping("/criar")
    public ResponseEntity<Map<String, String>> criarCartao(
            @Parameter(
                    description = "Dados para criação do cartão",
                    required = true,
                    schema = @Schema(implementation = CriarCartaoRequest.class)
            )
            @Valid @RequestBody CriarCartaoRequest request) throws Exception {

        CartaoDomain cartao = criarCartaoUseCase.criar(request.getNumeroCartao(), request.getSenha());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("numeroCartao", cartao.getNumeroCartao()));
    }
}