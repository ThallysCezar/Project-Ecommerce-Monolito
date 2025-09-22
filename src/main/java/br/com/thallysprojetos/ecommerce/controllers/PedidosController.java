package br.com.thallysprojetos.ecommerce.controllers;

import br.com.thallysprojetos.ecommerce.dtos.PagamentoDTO;
import br.com.thallysprojetos.ecommerce.dtos.pedidos.PedidosDTO;
import br.com.thallysprojetos.ecommerce.services.PedidosService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@AllArgsConstructor
public class PedidosController {

    private final PedidosService service;

    @GetMapping
    public ResponseEntity<Page<PedidosDTO>> findAll(@PageableDefault(size = 10) Pageable page) {
        return ResponseEntity.ok().body(service.findAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidosDTO> findPedidoById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<PedidosDTO>> findUserById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok().body(service.findByUserId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PedidosDTO> save(@Valid @RequestBody PedidosDTO dto, UriComponentsBuilder uriBuilder) {
        PedidosDTO pagamento = service.createPedido(dto);
        URI endereco = uriBuilder.path("/pedidos/{id}").buildAndExpand(pagamento.getId()).toUri();

        return ResponseEntity.created(endereco).body(pagamento);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PedidosDTO> updatePedidos(@Valid @PathVariable Long id, @RequestBody PedidosDTO dto) {
        service.updatePedidos(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmarPedido")
    public void confirmarPedidos(@PathVariable Long id) {
        service.confirmarPedidos(id);
    }

    @PostMapping("/{idPedido}/pagamento")
    public ResponseEntity<Void> adicionarPagamentoAoPedido(@PathVariable Long idPedido, @RequestBody @Valid PagamentoDTO pagamentoDto) {
        service.processarPagamentoDoPedido(idPedido, pagamentoDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePedido(@Valid @PathVariable Long id) {
        service.deletePedidos(id);
    }

    @PatchMapping("/cancelarPedido/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelarPedido(@Valid @PathVariable Long id) {
        service.cancelarPedido(id);
    }

}