package br.com.thallysprojetos.ecommerce.repositories;

import br.com.thallysprojetos.ecommerce.models.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutosRepository extends JpaRepository<Produtos, Long> {
}