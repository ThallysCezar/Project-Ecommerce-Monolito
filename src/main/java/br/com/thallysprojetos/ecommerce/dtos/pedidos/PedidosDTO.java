package br.com.thallysprojetos.ecommerce.dtos.pedidos;

import br.com.thallysprojetos.ecommerce.dtos.UsuariosDTO;
import br.com.thallysprojetos.ecommerce.dtos.pagamentos.PagamentoDTO;
import br.com.thallysprojetos.ecommerce.models.Usuarios;
import br.com.thallysprojetos.ecommerce.models.enums.StatusPedidos;
import br.com.thallysprojetos.ecommerce.models.pagamentos.Pagamento;
import br.com.thallysprojetos.ecommerce.models.pedidos.ItemDoPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidosDTO {

    private Long id;
    private LocalDateTime dataHora;
    private StatusPedidos statusPedidos;
    private List<ItemDoPedidoDTO> itens;
    private UsuarioIdDTO usuario;
    private PagamentoDTO pagamento;

}