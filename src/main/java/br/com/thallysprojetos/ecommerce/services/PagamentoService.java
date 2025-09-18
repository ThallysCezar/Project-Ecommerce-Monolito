package br.com.thallysprojetos.ecommerce.services;


import br.com.thallysprojetos.ecommerce.dtos.pagamentos.PagamentoDTO;
import br.com.thallysprojetos.ecommerce.models.enums.StatusPagamento;
import br.com.thallysprojetos.ecommerce.models.pagamentos.Pagamento;
import br.com.thallysprojetos.ecommerce.repositories.PagamentoRepository;
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
    private final ModelMapper modelMapper;

    public Page<PagamentoDTO> findAll(Pageable page) {
        return pagamentoRepository.findAll(page).map(p -> modelMapper.map(p, PagamentoDTO.class));
    }

    public PagamentoDTO findById(Long id) {
        return pagamentoRepository.findById(id)
                .map(p -> modelMapper.map(p, PagamentoDTO.class))
                .orElseThrow(NoSuchElementException::new);
    }

    public PagamentoDTO findByIdPedido(Long idPedido) {
        return pagamentoRepository.findByPedidoId(idPedido)
                .map(p -> modelMapper.map(p, PagamentoDTO.class))
                .orElseThrow(NoSuchElementException::new);
    }

    public PagamentoDTO updatePagamento(Long id, PagamentoDTO dto) {
        try {
            Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
            pagamento.setId(id);
            pagamento = pagamentoRepository.save(pagamento);

            return modelMapper.map(pagamento, PagamentoDTO.class);
        } catch (Exception exUser) {
            throw new NoSuchElementException("Pagamento não encontrado.");
        }
    }

    public void deletePagamento(Long id) {
        if (!pagamentoRepository.existsById(id)) {
            throw new NoSuchElementException(String.format("Pagamento não encontrado com o id '%s'.", id));
        }
        pagamentoRepository.deleteById(id);
    }

    public void processarPagamento(Long id) {

        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pagamento não encontrado com o ID: " + id));

        if (pagamento.getStatus().equals(StatusPagamento.CRIADO)) {
            pagamento.setStatus(StatusPagamento.CONFIRMADO);
        } else {
            pagamento.setStatus(StatusPagamento.CANCELADO);
        }

        pagamentoRepository.save(pagamento);
    }

}