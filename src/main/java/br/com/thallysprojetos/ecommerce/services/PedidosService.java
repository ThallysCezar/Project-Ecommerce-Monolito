package br.com.thallysprojetos.ecommerce.services;


import br.com.thallysprojetos.ecommerce.dtos.pagamentos.PagamentoDTO;
import br.com.thallysprojetos.ecommerce.dtos.pedidos.ItemDoPedidoDTO;
import br.com.thallysprojetos.ecommerce.dtos.pedidos.PedidosDTO;
import br.com.thallysprojetos.ecommerce.exceptions.pedidos.PedidosNotFoundException;
import br.com.thallysprojetos.ecommerce.exceptions.produtos.ProdutosNotFoundException;
import br.com.thallysprojetos.ecommerce.exceptions.usuarios.UsuarioNotFoundException;
import br.com.thallysprojetos.ecommerce.models.Produtos;
import br.com.thallysprojetos.ecommerce.models.Usuarios;
import br.com.thallysprojetos.ecommerce.models.enums.StatusPagamento;
import br.com.thallysprojetos.ecommerce.models.enums.StatusPedidos;
import br.com.thallysprojetos.ecommerce.models.enums.TipoFormaPagamento;
import br.com.thallysprojetos.ecommerce.models.pagamentos.Pagamento;
import br.com.thallysprojetos.ecommerce.models.pedidos.ItemDoPedido;
import br.com.thallysprojetos.ecommerce.models.pedidos.Pedidos;
import br.com.thallysprojetos.ecommerce.repositories.PagamentoRepository;
import br.com.thallysprojetos.ecommerce.repositories.PedidosRepository;
import br.com.thallysprojetos.ecommerce.repositories.ProdutosRepository;
import br.com.thallysprojetos.ecommerce.repositories.UsuariosRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PedidosService {

    private final PedidosRepository pedidosRepository;
    private final UsuariosRepository usuarioRepository;
    private final ProdutosRepository produtosRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ModelMapper modelMapper;

    public Page<PedidosDTO> findAll(Pageable page) {
        return pedidosRepository.findAll(page).map(p -> modelMapper.map(p, PedidosDTO.class));
    }

    public PedidosDTO findById(Long id) {
        return pedidosRepository.findById(id)
                .map(p -> modelMapper.map(p, PedidosDTO.class))
                .orElseThrow(PedidosNotFoundException::new);
    }

    public List<PedidosDTO> findByUserId(Long id) {
        List<Pedidos> pedidos = pedidosRepository.findByUsuarioId(id);
        if (pedidos.isEmpty()) {
            throw new PedidosNotFoundException("Nenhum pedido encontrado para o usuário fornecido.");
        }
        return pedidos.stream()
                .map(p -> modelMapper.map(p, PedidosDTO.class))
                .collect(Collectors.toList());
    }

    public PedidosDTO createPedido(PedidosDTO dto) {

        Usuarios usuario = usuarioRepository.findById(dto.getUsuario().getId())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));

        Pedidos novoPedido = modelMapper.map(dto, Pedidos.class);

        novoPedido.setUsuario(usuario);
        novoPedido.setStatusPedidos(StatusPedidos.CRIADO);
        novoPedido.setDataHora(LocalDateTime.now());


        novoPedido.setItens(new ArrayList<>());


        for (ItemDoPedidoDTO itemDto : dto.getItens()) {
            Produtos produto = produtosRepository.findById(itemDto.getProduto().getId())
                    .orElseThrow(() -> new ProdutosNotFoundException("Produto não encontrado com o ID: " + itemDto.getProduto().getId()));

            ItemDoPedido novoItem = new ItemDoPedido();
            novoItem.setQuantidade(itemDto.getQuantidade());
            novoItem.setDescricao(itemDto.getDescricao());
            novoItem.setProduto(produto);

            novoItem.setPedido(novoPedido);

            novoPedido.getItens().add(novoItem);
        }

        Pedidos pedidoSalvo = pedidosRepository.save(novoPedido);
        return modelMapper.map(pedidoSalvo, PedidosDTO.class);
    }

    @Transactional
    public void processarPagamentoDoPedido(Long idPedido, PagamentoDTO pagamentoDto) {
        Pedidos pedido = pedidosRepository.findById(idPedido)
                .orElseThrow(() -> new PedidosNotFoundException("Pedido não encontrado."));

        Pagamento novoPagamento = modelMapper.map(pagamentoDto, Pagamento.class);
        novoPagamento.setStatus(StatusPagamento.CRIADO);

        if (pagamentoDto.getTipoPagamento().equals(TipoFormaPagamento.BOLETO) && pagamentoDto.getCodigoDeBarrasBoleto() == null) {
            novoPagamento.setCodigoDeBarrasBoleto("12345678901234567890");
        }

        Pagamento pagamentoSalvo = pagamentoRepository.save(novoPagamento);

        pedido.setPagamento(pagamentoSalvo);
        pedido.setStatusPedidos(StatusPedidos.AGUARDANDO_CONFIRMACAO);
        pedidosRepository.save(pedido);
    }

    public PedidosDTO updatePedidos(Long id, PedidosDTO dto) {
        try {
            Pedidos pedidos = modelMapper.map(dto, Pedidos.class);
            pedidos.setId(id);
            pedidos.setStatusPedidos(dto.getStatusPedidos());
            pedidos.setItens(modelMapper.map(dto, Pedidos.class).getItens());

            Pedidos finalPedidos = pedidos;
            pedidos.getItens().forEach(item -> item.setPedido(finalPedidos));
            pedidos = pedidosRepository.save(pedidos);

            return modelMapper.map(pedidos, PedidosDTO.class);
        } catch (Exception exUser) {
            throw new PedidosNotFoundException("Pedidos não encontrado.");
        }
    }

    public void deletePedidos(Long id) {
        if (!pedidosRepository.existsById(id)) {
            throw new PedidosNotFoundException(String.format("Pedidos não encontrado com o id '%s'.", id));
        }
        pedidosRepository.deleteById(id);
    }

    public void confirmarPedidos(Long id) {
        Pedidos pedido = pedidosRepository.findById(id)
                .orElseThrow(() -> new PedidosNotFoundException("Pedido não encontrado"));

        if (pedido.getPagamento().getStatus() == StatusPagamento.CONFIRMADO) {
            pedido.setStatusPedidos(StatusPedidos.PAGO);
        } else {
            pedido.setStatusPedidos(StatusPedidos.AGUARDANDO_CONFIRMACAO);
        }

        pedidosRepository.save(pedido);
    }

    public void cancelarPedido(Long id) {
        Pedidos pedido = pedidosRepository.findById(id)
                .orElseThrow(() -> new PedidosNotFoundException("Pedido não encontrado"));

        pedido.setStatusPedidos(StatusPedidos.CANCELADO);
        pedidosRepository.save(pedido);
    }

}