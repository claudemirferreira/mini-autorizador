package com.vr.mini.autorizador.presentation.controller;

import com.vr.mini.autorizador.application.usecase.ConsultarSaldoCartaoUseCase;
import com.vr.mini.autorizador.application.usecase.CriarCartaoUseCase;
import com.vr.mini.autorizador.domain.CartaoDomain;
import com.vr.mini.autorizador.presentation.dto.CriarCartaoRequest;
import com.vr.mini.autorizador.presentation.dto.SaldoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
@Tag(name = "Cartões", description = "Operações para gestão de cartões e saldos")
public class CartaoController {
    private final CriarCartaoUseCase criarCartaoUseCase;
    private final ConsultarSaldoCartaoUseCase consultarSaldoCartaoUseCase;

    @Operation(
            summary = "Cria um novo cartão",
            description = """
            Cria um novo cartão com saldo inicial de R$ 500,00.
            O número do cartão deve ter 16 dígitos e a senha entre 4-6 caracteres.
            """,
            tags = { "Cartões" }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cartão criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SaldoResponse.class),
                            examples = @ExampleObject(
                                    name = "cartao_criado",
                                    value = """
                        {
                            "numeroCartao": "6549873025634501",
                            "saldo": 500.00,
                            "saldoFormatado": "R$ 500,00"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Conflito - Cartão já existe",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "cartao_existente",
                                    value = "{ \"mensagem\": \"Cartão já cadastrado\" }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "cartao_invalido",
                                            value = "{ \"erro\": \"Número do cartão deve ter 16 dígitos\" }"
                                    ),
                                    @ExampleObject(
                                            name = "senha_invalida",
                                            value = "{ \"erro\": \"Senha deve ter entre 4 e 6 dígitos\" }"
                                    )
                            }
                    )
            )
    })
    @PostMapping("/criar")
    public ResponseEntity<SaldoResponse> criarCartao(
            @Parameter(
                    description = "Dados para criação do cartão",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CriarCartaoRequest.class))
            )
            @Valid @RequestBody CriarCartaoRequest request) {

        CartaoDomain cartao = criarCartaoUseCase.execute(request.getNumeroCartao(), request.getSenha());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SaldoResponse(
                        cartao.getNumeroCartao(),
                        cartao.getSaldo()
                ));
    }

    @Operation(
            summary = "Consulta saldo do cartão",
            description = "Retorna o saldo disponível para um cartão existente",
            tags = { "Cartões" }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Saldo retornado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SaldoResponse.class),
                            examples = @ExampleObject(
                                    name = "saldo_sucesso",
                                    value = """
                        {
                            "numeroCartao": "6549873025634501",
                            "saldo": 500.00,
                            "saldoFormatado": "R$ 500,00"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cartão não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "cartao_nao_encontrado",
                                    value = "{ \"mensagem\": \"Cartão não encontrado\" }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Número de cartão inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "numero_invalido",
                                    value = "{ \"erro\": \"Número do cartão deve ter 16 dígitos\" }"
                            )
                    )
            )
    })
    @GetMapping("/{numeroCartao}")
    public ResponseEntity<SaldoResponse> consultarSaldo(
            @Parameter(
                    description = "Número do cartão (16 dígitos)",
                    example = "6549873025634501",
                    required = true)
            @PathVariable String numeroCartao) {

        CartaoDomain cartaoDomain = consultarSaldoCartaoUseCase.execute(numeroCartao);
        return ResponseEntity.ok(new SaldoResponse(
                cartaoDomain.getNumeroCartao(),
                cartaoDomain.getSaldo()
        ));
    }
}