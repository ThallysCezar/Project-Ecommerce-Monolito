package br.com.thallysprojetos.ecommerce.exceptions.pedidos;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PedidosException extends ResponseStatusException {

    public PedidosException(){
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Erro geral model");
    }

    public PedidosException(String message){
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

}
