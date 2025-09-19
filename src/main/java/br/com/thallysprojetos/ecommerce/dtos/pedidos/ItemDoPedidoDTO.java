package br.com.thallysprojetos.ecommerce.dtos.pedidos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemDoPedidoDTO {

    private Long id;
    private Integer quantidade;
    private String descricao;
    private ProdutoIdDTO produto;

}