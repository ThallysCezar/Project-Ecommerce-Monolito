package br.com.thallysprojetos.ecommerce.services;


import br.com.thallysprojetos.ecommerce.dtos.PagamentoDTO;
import br.com.thallysprojetos.ecommerce.exceptions.pagamento.PagamentoNotFoundException;
import br.com.thallysprojetos.ecommerce.exceptions.pedidos.PedidosNotFoundException;
import br.com.thallysprojetos.ecommerce.models.enums.StatusPagamento;
import br.com.thallysprojetos.ecommerce.models.pagamentos.Pagamento;
import br.com.thallysprojetos.ecommerce.repositories.PagamentoRepository;
import br.com.thallysprojetos.ecommerce.repositories.PedidosRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PedidosRepository pedidosRepository;
    private final ModelMapper modelMapper;

    public Page<PagamentoDTO> findAll(Pageable page) {
        return pagamentoRepository.findAll(page).map(p -> modelMapper.map(p, PagamentoDTO.class));
    }

    public PagamentoDTO findById(Long id) {
        return pagamentoRepository.findById(id)
                .map(p -> modelMapper.map(p, PagamentoDTO.class))
                .orElseThrow(PagamentoNotFoundException::new);
    }

    public PagamentoDTO findByPedidoId(Long idPedido) {
        return pedidosRepository.findById(idPedido)
                .map(pedido -> {
                    if (pedido.getPagamento() == null) {
                        throw new PagamentoNotFoundException("Nenhum pagamento encontrado para este pedido.");
                    }
                    return modelMapper.map(pedido.getPagamento(), PagamentoDTO.class);
                })
                .orElseThrow(() -> new PedidosNotFoundException("Pedido não encontrado com o ID: " + idPedido));
    }

    public PagamentoDTO updatePagamento(Long id, PagamentoDTO dto) {
        Pagamento pagamentoExistente = pagamentoRepository.findById(id)
                .orElseThrow(() -> new PagamentoNotFoundException("Pagamento não encontrado com o ID: " + id));

        if (dto.getValor() != null) {
            pagamentoExistente.setValor(dto.getValor());
        }

        Pagamento pagamentoAtualizado = pagamentoRepository.save(pagamentoExistente);
        return modelMapper.map(pagamentoAtualizado, PagamentoDTO.class);
    }

    public void deletePagamento(Long id) {
        if (!pagamentoRepository.existsById(id)) {
            throw new PagamentoNotFoundException(String.format("Pagamento não encontrado com o id '%s'.", id));
        }
        pagamentoRepository.deleteById(id);
    }

    public void processarPagamento(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new PagamentoNotFoundException("Pagamento não encontrado com o ID: " + id));

        if (pagamento.getStatus().equals(StatusPagamento.CRIADO)) {
            pagamento.setStatus(StatusPagamento.CONFIRMADO);
        } else {
            pagamento.setStatus(StatusPagamento.CANCELADO);
        }

        pagamentoRepository.save(pagamento);
    }

}