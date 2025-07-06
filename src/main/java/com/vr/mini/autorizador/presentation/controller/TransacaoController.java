package com.vr.mini.autorizador.presentation.controller;

import com.vr.mini.autorizador.application.usecase.DebitarUseCase;
import com.vr.mini.autorizador.presentation.dto.TransacaoRequest;
import com.vr.mini.autorizador.presentation.dto.TransacaoResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Operações relacionadas a transações financeiras")
public class TransacaoController {

    private final DebitarUseCase debitarUseCase;

    @Operation(
            summary = "Realizar transação",
            description = """
            Processa uma transação financeira de débito no cartão.
            Requer número do cartão, senha e valor da transação.
            Valida saldo suficiente e credenciais antes de processar.
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Transação autorizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransacaoResponse.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                            "mensagem": "Operação realizada com sucesso",
                            "dataHora": "2023-08-15T14:30:45Z",
                            "valor": 150.00
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Transação não autorizada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "saldo_insuficiente",
                                            value = """
                            {
                                "mensagem": "SALDO_INSUFICIENTE",
                                "dataHora": "2023-08-15T14:30:45Z",
                                "valor": 150.00
                            }
                            """
                                    ),
                                    @ExampleObject(
                                            name = "senha_invalida",
                                            value = """
                            {
                                "mensagem": "SENHA_INVALIDA",
                                "dataHora": "2023-08-15T14:30:45Z",
                                "valor": 150.00
                            }
                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                            "erro": "Valor da transação deve ser positivo"
                        }
                        """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<TransacaoResponse> realizarTransacao(
            @Parameter(
                    description = "Dados da transação",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TransacaoRequest.class))
            )
            @Valid @RequestBody TransacaoRequest request) {

        debitarUseCase.execute(
                request.numeroCartao(),
                request.senhaCartao(),
                request.valor()
        );

        var response = new TransacaoResponse(
                "Operação realizada com sucesso",
                OffsetDateTime.now(),
                request.valor()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}