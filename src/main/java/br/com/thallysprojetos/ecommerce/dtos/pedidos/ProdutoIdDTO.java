package br.com.thallysprojetos.ecommerce.dtos.pedidos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoIdDTO {

    @NotNull
    @Positive
    private Long id;

}