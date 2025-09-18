package br.com.thallysprojetos.ecommerce.exceptions;

import br.com.thallysprojetos.ecommerce.exceptions.pagamento.PagamentoAlreadyExistException;
import br.com.thallysprojetos.ecommerce.exceptions.pagamento.PagamentoException;
import br.com.thallysprojetos.ecommerce.exceptions.pagamento.PagamentoNotFoundException;
import br.com.thallysprojetos.ecommerce.exceptions.pedidos.PedidosAlreadyExistException;
import br.com.thallysprojetos.ecommerce.exceptions.pedidos.PedidosException;
import br.com.thallysprojetos.ecommerce.exceptions.pedidos.PedidosNotFoundException;
import br.com.thallysprojetos.ecommerce.exceptions.produtos.ProdutosAlreadyExistException;
import br.com.thallysprojetos.ecommerce.exceptions.produtos.ProdutosException;
import br.com.thallysprojetos.ecommerce.exceptions.produtos.ProdutosNotFoundException;
import br.com.thallysprojetos.ecommerce.exceptions.usuarios.UsuarioAlreadyExistException;
import br.com.thallysprojetos.ecommerce.exceptions.usuarios.UsuarioException;
import br.com.thallysprojetos.ecommerce.exceptions.usuarios.UsuarioNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

//    private final Logger LOGGER = LogManager.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler({PagamentoNotFoundException.class, PedidosNotFoundException.class, ProdutosNotFoundException.class, UsuarioNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleNotFoundException(Exception ex, HttpServletRequest request) {
        return handleException(ex, request, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({PagamentoAlreadyExistException.class, PedidosAlreadyExistException.class, ProdutosAlreadyExistException.class, UsuarioAlreadyExistException.class})
    public ResponseEntity<ErrorMessage> handleAlreadyExistException(Exception ex, HttpServletRequest request) {
        return handleException(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({PagamentoException.class, PedidosException.class, ProdutosException.class, UsuarioException.class})
    public ResponseEntity<ErrorMessage> handleGeneralException(Exception ex, HttpServletRequest request) {
        return handleException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorMessage> handleException(Exception ex, HttpServletRequest request, HttpStatus status) {
        String messageError = "Api Error: ";
//        LOGGER.error(messageError, ex);
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, status, ex.getMessage()));
    }

}