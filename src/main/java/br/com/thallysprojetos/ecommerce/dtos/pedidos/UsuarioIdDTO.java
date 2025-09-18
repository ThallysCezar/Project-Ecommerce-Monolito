package br.com.thallysprojetos.ecommerce.dtos.pedidos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UsuarioIdDTO {

    @NotNull
    @Positive
    private Long id;

}