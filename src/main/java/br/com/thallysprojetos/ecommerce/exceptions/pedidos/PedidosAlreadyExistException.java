package br.com.thallysprojetos.ecommerce.exceptions.pedidos;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PedidosAlreadyExistException extends ResponseStatusException {

    public PedidosAlreadyExistException(){
        super(HttpStatus.CONFLICT, "Já existe esse pedido");
    }

    public PedidosAlreadyExistException(String message){
        super(HttpStatus.CONFLICT, message);
    }

}