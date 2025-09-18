package br.com.thallysprojetos.ecommerce.services;


import br.com.thallysprojetos.ecommerce.dtos.ProdutosDTO;
import br.com.thallysprojetos.ecommerce.models.Produtos;
import br.com.thallysprojetos.ecommerce.repositories.ProdutosRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProdutosService {

    private final ProdutosRepository produtosRepository;
    private final ModelMapper modelMapper;

    public Page<ProdutosDTO> findAll(Pageable page) {
        return produtosRepository.findAll(page).map(p -> modelMapper.map(p, ProdutosDTO.class));
    }

    public ProdutosDTO findProductById(Long id) {
        return produtosRepository.findById(id)
                .map(p -> modelMapper.map(p, ProdutosDTO.class))
                .orElseThrow(NoSuchElementException::new);
    }

    public ProdutosDTO createProduct(ProdutosDTO dto) {
        try {
            Produtos produtos = modelMapper.map(dto, Produtos.class);

            Produtos produtosSaved = produtosRepository.save(produtos);

            return modelMapper.map(produtosSaved, ProdutosDTO.class);
        } catch (Exception exProdutos) {
            //Consertar essa parte do exception
            throw new NoSuchElementException();
        }
    }

    public List<ProdutosDTO> createProducts(List<ProdutosDTO> dtos) {
        return dtos.stream()
                .map(dto -> {
                    Produtos produto = modelMapper.map(dto, Produtos.class);
                    Produtos produtoSalvo = produtosRepository.save(produto);
                    return modelMapper.map(produtoSalvo, ProdutosDTO.class);
                })
                .collect(Collectors.toList());
    }

    public ProdutosDTO updateProdutos(Long id, ProdutosDTO dto) {
        try {
            Produtos produtos = modelMapper.map(dto, Produtos.class);
            produtos.setId(id);
            produtos = produtosRepository.save(produtos);

            return modelMapper.map(produtos, ProdutosDTO.class);
        } catch (Exception exUser) {
            throw new NoSuchElementException("Produtos não encontrado.");
        }
    }

    public void deleteProdutos(Long id) {
        if (!produtosRepository.existsById(id)) {
            throw new NoSuchElementException(String.format("Produtos não encontrado com o id '%s'.", id));
        }
        produtosRepository.deleteById(id);
    }

}