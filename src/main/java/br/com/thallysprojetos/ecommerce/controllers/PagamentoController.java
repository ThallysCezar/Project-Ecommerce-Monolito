package br.com.thallysprojetos.ecommerce.controllers;

import br.com.thallysprojetos.ecommerce.dtos.pagamentos.PagamentoDTO;
import br.com.thallysprojetos.ecommerce.services.PagamentoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
@AllArgsConstructor
//@SecurityRequirement(name = "Bearer Authentication")
public class PagamentoController {

    private final PagamentoService service;

    @GetMapping
    public ResponseEntity<Page<PagamentoDTO>> findAll(@PageableDefault(size = 10) Pageable page) {
        return ResponseEntity.ok().body(service.findAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> findPagamentoById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<PagamentoDTO> findPagamentoByPedido(@Valid @PathVariable Long idPedido) {
        return ResponseEntity.ok().body(service.findByPedidoId(idPedido));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PagamentoDTO> updatePagamento(@Valid @PathVariable Long id, @RequestBody PagamentoDTO dto) {
        service.updatePagamento(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
//    @CircuitBreaker(name = "atualizaPedido", fallbackMethod = "pagamentoAutorizadoComIntegracaoPendente")
    public void confirmarPagamento(@PathVariable Long id) {
        service.processarPagamento(id);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePagamento(@Valid @PathVariable Long id) {
        service.deletePagamento(id);
    }

}