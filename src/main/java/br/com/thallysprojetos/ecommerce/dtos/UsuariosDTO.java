package br.com.thallysprojetos.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuariosDTO {

    private Long id;
    private String userName;
    private String email;
    private String password;

}