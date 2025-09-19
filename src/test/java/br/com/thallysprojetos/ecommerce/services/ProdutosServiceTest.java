package br.com.thallysprojetos.ecommerce.services;

import br.com.thallysprojetos.ecommerce.dtos.ProdutosDTO;
import br.com.thallysprojetos.ecommerce.exceptions.produtos.ProdutosNotFoundException;
import br.com.thallysprojetos.ecommerce.models.Produtos;
import br.com.thallysprojetos.ecommerce.repositories.ProdutosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutosServiceTest {

    @Mock
    private ProdutosRepository produtosRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProdutosService produtosService;

    private Produtos produto;
    private ProdutosDTO produtoDTO;

    @BeforeEach
    void setUp() {
        produto = new Produtos();
        produto.setId(1L);
        produto.setTitulo("Produto Teste");
        produto.setPreco(100.0);

        produtoDTO = new ProdutosDTO();
        produtoDTO.setId(1L);
        produtoDTO.setTitulo("Produto Teste");
        produtoDTO.setPreco(100.0);
    }

    @Test
    @DisplayName("Deve retornar uma página de produtos ao chamar findAll")
    void findAll_ShouldReturnPageOfProdutosDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produtos> page = new PageImpl<>(Collections.singletonList(produto));

        when(produtosRepository.findAll(pageable)).thenReturn(page);
        when(modelMapper.map(any(Produtos.class), eq(ProdutosDTO.class))).thenReturn(produtoDTO);

        Page<ProdutosDTO> result = produtosService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(produtosRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve encontrar um produto por ID e retornar ProdutosDTO")
    void findProductById_ShouldReturnProdutosDTO_WhenProductExists() {
        when(produtosRepository.findById(anyLong())).thenReturn(Optional.of(produto));
        when(modelMapper.map(any(Produtos.class), eq(ProdutosDTO.class))).thenReturn(produtoDTO);

        ProdutosDTO result = produtosService.findProductById(1L);

        assertNotNull(result);
        assertEquals(produtoDTO.getId(), result.getId());
        verify(produtosRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar produto por ID que não existe")
    void findProductById_ShouldThrowException_WhenProductDoesNotExist() {
        when(produtosRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProdutosNotFoundException.class, () -> produtosService.findProductById(1L));
        verify(produtosRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve criar um novo produto com sucesso")
    void createProduct_ShouldCreateAndReturnProdutosDTO() {
        when(modelMapper.map(any(ProdutosDTO.class), eq(Produtos.class))).thenReturn(produto);
        when(produtosRepository.save(any(Produtos.class))).thenReturn(produto);
        when(modelMapper.map(any(Produtos.class), eq(ProdutosDTO.class))).thenReturn(produtoDTO);

        ProdutosDTO result = produtosService.createProduct(produtoDTO);

        assertNotNull(result);
        assertEquals(produtoDTO.getId(), result.getId());
        verify(produtosRepository, times(1)).save(any(Produtos.class));
    }

    @Test
    @DisplayName("Deve criar múltiplos produtos em lote com sucesso")
    void createProducts_ShouldCreateMultipleProducts() {
        List<ProdutosDTO> dtos = Collections.singletonList(produtoDTO);
        List<Produtos> produtos = Collections.singletonList(produto);

        when(modelMapper.map(any(ProdutosDTO.class), eq(Produtos.class))).thenReturn(produto);
        when(produtosRepository.save(any(Produtos.class))).thenReturn(produto);
        when(modelMapper.map(any(Produtos.class), eq(ProdutosDTO.class))).thenReturn(produtoDTO);

        List<ProdutosDTO> result = produtosService.createProducts(dtos);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(produtosRepository, times(1)).save(any(Produtos.class));
    }

    @Test
    @DisplayName("Deve atualizar um produto com sucesso")
    void updateProdutos_ShouldUpdateProduct_WhenProductExists() {
        when(produtosRepository.findById(anyLong())).thenReturn(Optional.of(produto));
        when(produtosRepository.save(any(Produtos.class))).thenReturn(produto);

        when(modelMapper.map(any(Produtos.class), eq(ProdutosDTO.class))).thenAnswer(invocation -> {
            Produtos source = invocation.getArgument(0);
            ProdutosDTO destination = new ProdutosDTO();
            destination.setId(source.getId());
            destination.setTitulo(source.getTitulo());
            return destination;
        });

        ProdutosDTO updatedDto = new ProdutosDTO();
        updatedDto.setId(1L);
        updatedDto.setTitulo("Título Atualizado");

        ProdutosDTO result = produtosService.updateProdutos(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Título Atualizado", result.getTitulo());
        verify(produtosRepository, times(1)).findById(1L);
        verify(produtosRepository, times(1)).save(any(Produtos.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar produto que não existe")
    void updateProdutos_ShouldThrowException_WhenProductDoesNotExist() {
        when(produtosRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProdutosNotFoundException.class, () -> produtosService.updateProdutos(1L, produtoDTO));
        verify(produtosRepository, times(1)).findById(1L);
        verify(produtosRepository, never()).save(any(Produtos.class));
    }

    @Test
    @DisplayName("Deve deletar um produto com sucesso")
    void deleteProdutos_ShouldDeleteProduct_WhenProductExists() {
        when(produtosRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(produtosRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> produtosService.deleteProdutos(1L));
        verify(produtosRepository, times(1)).existsById(1L);
        verify(produtosRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar produto que não existe")
    void deleteProdutos_ShouldThrowException_WhenProductDoesNotExist() {
        when(produtosRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ProdutosNotFoundException.class, () -> produtosService.deleteProdutos(1L));
        verify(produtosRepository, times(1)).existsById(1L);
        verify(produtosRepository, never()).deleteById(anyLong());
    }

}