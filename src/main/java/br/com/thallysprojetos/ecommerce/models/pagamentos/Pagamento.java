package br.com.thallysprojetos.ecommerce.models.pagamentos;

import br.com.thallysprojetos.ecommerce.models.enums.StatusPagamento;
import br.com.thallysprojetos.ecommerce.models.enums.TipoFormaPagamento;
import br.com.thallysprojetos.ecommerce.models.pedidos.Pedidos;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double valor;

    @NotNull
    private StatusPagamento status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoFormaPagamento tipoPagamento;

    private String nomeTitularCartao;
    private String numeroCartao;
    private String expiracaoCartao;
    private String codigoCartao;

    private String codigoDeBarrasBoleto;

    private String chavePix;

    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedidos pedido;

}