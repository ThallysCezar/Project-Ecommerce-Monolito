package br.com.thallysprojetos.ecommerce.repositories;

import br.com.thallysprojetos.ecommerce.models.pedidos.Pedidos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidosRepository extends JpaRepository<Pedidos, Long> {

    List<Pedidos> findByUsuarioId(Long idUsuario);

}