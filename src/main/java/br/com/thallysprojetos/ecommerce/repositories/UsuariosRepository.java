package br.com.thallysprojetos.ecommerce.repositories;

import br.com.thallysprojetos.ecommerce.models.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findByEmail(String email);

}