package br.com.thallysprojetos.ecommerce.repositories;

import br.com.thallysprojetos.ecommerce.dtos.pagamentos.PagamentoDTO;
import br.com.thallysprojetos.ecommerce.models.pagamentos.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    // Corrigir para não precisar ver essa parte de Optional aqui.
    Optional<PagamentoDTO> findByPedidoId(Long idPedido);

}