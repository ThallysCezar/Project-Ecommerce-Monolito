package br.com.thallysprojetos.ecommerce.services;

import br.com.thallysprojetos.ecommerce.dtos.PagamentoDTO;
import br.com.thallysprojetos.ecommerce.exceptions.pagamento.PagamentoNotFoundException;
import br.com.thallysprojetos.ecommerce.exceptions.pedidos.PedidosNotFoundException;
import br.com.thallysprojetos.ecommerce.models.enums.StatusPagamento;
import br.com.thallysprojetos.ecommerce.models.pagamentos.Pagamento;
import br.com.thallysprojetos.ecommerce.models.pedidos.Pedidos;
import br.com.thallysprojetos.ecommerce.repositories.PagamentoRepository;
import br.com.thallysprojetos.ecommerce.repositories.PedidosRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private PedidosRepository pedidosRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Pagamento pagamento;
    private PagamentoDTO pagamentoDTO;
    private Pedidos pedido;

    @BeforeEach
    void setUp() {
        pagamento = new Pagamento(1L, 100.0, StatusPagamento.CRIADO, null, null, null, null, null, null, null, null);
        pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setValor(100.0);
        pagamentoDTO.setStatus(StatusPagamento.CRIADO);

        pedido = new Pedidos();
        pedido.setId(1L);
        pedido.setPagamento(pagamento);
    }

    @Test
    @DisplayName("Deve retornar uma página de pagamentos ao chamar findAll")
    void findAll_ShouldReturnPageOfPagamentoDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pagamento> page = new PageImpl<>(Collections.singletonList(pagamento));

        when(pagamentoRepository.findAll(pageable)).thenReturn(page);
        when(modelMapper.map(any(Pagamento.class), eq(PagamentoDTO.class))).thenReturn(pagamentoDTO);

        Page<PagamentoDTO> result = pagamentoService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(pagamentoRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve encontrar um pagamento por ID e retornar PagamentoDTO")
    void findById_ShouldReturnPagamentoDTO_WhenPagamentoExists() {
        when(pagamentoRepository.findById(anyLong())).thenReturn(Optional.of(pagamento));
        when(modelMapper.map(any(Pagamento.class), eq(PagamentoDTO.class))).thenReturn(pagamentoDTO);

        PagamentoDTO result = pagamentoService.findById(1L);

        assertNotNull(result);
        assertEquals(pagamentoDTO.getValor(), result.getValor());
        verify(pagamentoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar pagamento por ID que não existe")
    void findById_ShouldThrowException_WhenPagamentoDoesNotExist() {
        when(pagamentoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PagamentoNotFoundException.class, () -> pagamentoService.findById(1L));
        verify(pagamentoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve encontrar pagamento por ID de pedido e retornar PagamentoDTO")
    void findByPedidoId_ShouldReturnPagamentoDTO_WhenPedidoExistsAndHasPagamento() {
        when(pedidosRepository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(modelMapper.map(any(Pagamento.class), eq(PagamentoDTO.class))).thenReturn(pagamentoDTO);

        PagamentoDTO result = pagamentoService.findByPedidoId(1L);

        assertNotNull(result);
        assertEquals(pagamentoDTO.getValor(), result.getValor());
        verify(pedidosRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar pagamento por ID de pedido que não existe")
    void findByPedidoId_ShouldThrowException_WhenPedidoDoesNotExist() {
        when(pedidosRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PedidosNotFoundException.class, () -> pagamentoService.findByPedidoId(1L));
        verify(pedidosRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar pagamento por ID de pedido sem pagamento associado")
    void findByPedidoId_ShouldThrowException_WhenPagamentoIsNull() {
        pedido.setPagamento(null);
        when(pedidosRepository.findById(anyLong())).thenReturn(Optional.of(pedido));

        assertThrows(PagamentoNotFoundException.class, () -> pagamentoService.findByPedidoId(1L));
        verify(pedidosRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve atualizar um pagamento com sucesso")
    void updatePagamento_ShouldUpdatePagamento_WhenPagamentoExists() {
        when(pagamentoRepository.findById(anyLong())).thenReturn(Optional.of(pagamento));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        when(modelMapper.map(any(Pagamento.class), eq(PagamentoDTO.class))).thenAnswer(invocation -> {
            Pagamento source = invocation.getArgument(0);
            PagamentoDTO destination = new PagamentoDTO();
            destination.setValor(source.getValor());
            return destination;
        });

        PagamentoDTO updatedDto = new PagamentoDTO();
        updatedDto.setValor(200.0);

        PagamentoDTO result = pagamentoService.updatePagamento(1L, updatedDto);

        assertNotNull(result);
        assertEquals(200.0, result.getValor());
        verify(pagamentoRepository, times(1)).findById(1L);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar pagamento que não existe")
    void updatePagamento_ShouldThrowException_WhenPagamentoDoesNotExist() {
        when(pagamentoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PagamentoNotFoundException.class, () -> pagamentoService.updatePagamento(1L, pagamentoDTO));
        verify(pagamentoRepository, times(1)).findById(1L);
        verify(pagamentoRepository, never()).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Deve deletar um pagamento com sucesso")
    void deletePagamento_ShouldDeletePagamento_WhenPagamentoExists() {
        when(pagamentoRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(pagamentoRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> pagamentoService.deletePagamento(1L));
        verify(pagamentoRepository, times(1)).existsById(1L);
        verify(pagamentoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar pagamento que não existe")
    void deletePagamento_ShouldThrowException_WhenPagamentoDoesNotExist() {
        when(pagamentoRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(PagamentoNotFoundException.class, () -> pagamentoService.deletePagamento(1L));
        verify(pagamentoRepository, times(1)).existsById(1L);
        verify(pagamentoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve processar pagamento e mudar o status para CONFIRMADO")
    void processarPagamento_ShouldChangeStatusToConfirmado_WhenStatusIsCriado() {
        pagamento.setStatus(StatusPagamento.CRIADO);
        when(pagamentoRepository.findById(anyLong())).thenReturn(Optional.of(pagamento));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        pagamentoService.processarPagamento(1L);

        assertEquals(StatusPagamento.CONFIRMADO, pagamento.getStatus());
        verify(pagamentoRepository, times(1)).findById(1L);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Deve processar pagamento e mudar o status para CANCELADO")
    void processarPagamento_ShouldChangeStatusToCancelado_WhenStatusIsNotCriado() {
        pagamento.setStatus(StatusPagamento.CONFIRMADO);
        when(pagamentoRepository.findById(anyLong())).thenReturn(Optional.of(pagamento));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        pagamentoService.processarPagamento(1L);

        assertEquals(StatusPagamento.CANCELADO, pagamento.getStatus());
        verify(pagamentoRepository, times(1)).findById(1L);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao processar pagamento que não existe")
    void processarPagamento_ShouldThrowException_WhenPagamentoDoesNotExist() {
        when(pagamentoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PagamentoNotFoundException.class, () -> pagamentoService.processarPagamento(1L));
        verify(pagamentoRepository, times(1)).findById(1L);
        verify(pagamentoRepository, never()).save(any(Pagamento.class));
    }

}