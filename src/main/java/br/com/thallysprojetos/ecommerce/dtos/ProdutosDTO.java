package br.com.thallysprojetos.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutosDTO {

    private Long id;
    private String titulo;
    private String tipoProduto;
    private String descricao;
    private Double preco;
    private boolean itemEstoque;
    private Integer estoque;

}