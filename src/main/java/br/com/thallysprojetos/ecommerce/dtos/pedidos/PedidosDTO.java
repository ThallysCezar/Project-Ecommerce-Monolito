package br.com.thallysprojetos.ecommerce.dtos.pedidos;

import br.com.thallysprojetos.ecommerce.dtos.PagamentoDTO;
import br.com.thallysprojetos.ecommerce.models.enums.StatusPedidos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PedidosDTO {

    private Long id;
    private LocalDateTime dataHora;
    private StatusPedidos statusPedidos;
    private List<ItemDoPedidoDTO> itens;
    private UsuarioIdDTO usuario;
    private PagamentoDTO pagamento;

}