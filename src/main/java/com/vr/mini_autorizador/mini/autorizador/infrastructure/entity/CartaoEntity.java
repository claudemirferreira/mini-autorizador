package com.vr.mini_autorizador.mini.autorizador.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cartao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartaoEntity {
    @Id
    @Column(name = "numero_cartao", nullable = false, unique = true, length = 20)
    private String numeroCartao;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "saldo", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;
} 