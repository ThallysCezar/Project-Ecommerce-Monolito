package br.com.thallysprojetos.ecommerce.dtos.pedidos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDoPedidoDTO {

    private Long id;
    private Integer quantidade;
    private String descricao;
    private ProdutoIdDTO produto;

}