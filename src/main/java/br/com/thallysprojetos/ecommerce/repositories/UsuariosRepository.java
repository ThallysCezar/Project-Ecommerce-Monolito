package br.com.thallysprojetos.ecommerce.repositories;

import br.com.thallysprojetos.ecommerce.models.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {

    // Corrigir para não precisar ver essa parte de Optional aqui.
    Optional<Usuarios> findByEmail(String email);

}