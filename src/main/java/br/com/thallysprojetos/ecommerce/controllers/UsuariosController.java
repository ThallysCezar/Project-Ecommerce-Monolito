package br.com.thallysprojetos.ecommerce.controllers;

import br.com.thallysprojetos.ecommerce.dtos.UsuariosDTO;
import br.com.thallysprojetos.ecommerce.services.UsuariosService;
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

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuariosController {

    private final UsuariosService service;

    @GetMapping
    public ResponseEntity<Page<UsuariosDTO>> findAll(@PageableDefault(size = 10) Pageable page) {
        return ResponseEntity.ok().body(service.findAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuariosDTO> findUserById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuariosDTO> findUserByEmail(@Valid @PathVariable String email) {
        return ResponseEntity.ok().body(service.findByEmail(email));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UsuariosDTO> createUser(@Valid @RequestBody UsuariosDTO dto, UriComponentsBuilder uriBuilder) {
        UsuariosDTO pagamento = service.createUser(dto);
        URI endereco = uriBuilder.path("/usuarios/{id}").buildAndExpand(pagamento.getId()).toUri();

        return ResponseEntity.created(endereco).body(pagamento);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UsuariosDTO> updateUser(@Valid @PathVariable Long id, @RequestBody UsuariosDTO dto) {
        service.updateUsuarios(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Valid @PathVariable Long id) {
        service.deleteUsuarios(id);
    }

}