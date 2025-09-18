package br.com.thallysprojetos.ecommerce.repositories;

import br.com.thallysprojetos.ecommerce.models.pagamentos.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}