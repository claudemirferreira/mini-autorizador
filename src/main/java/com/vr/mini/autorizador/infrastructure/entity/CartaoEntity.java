package com.vr.mini.autorizador.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;

@Entity
@Table(name = "cartao")
@Data
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