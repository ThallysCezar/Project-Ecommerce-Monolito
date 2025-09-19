package br.com.thallysprojetos.ecommerce.services;

import br.com.thallysprojetos.ecommerce.dtos.PagamentoDTO;
import br.com.thallysprojetos.ecommerce.dtos.ProdutosDTO;
import br.com.thallysprojetos.ecommerce.dtos.pedidos.ItemDoPedidoDTO;
import br.com.thallysprojetos.ecommerce.dtos.pedidos.PedidosDTO;
import br.com.thallysprojetos.ecommerce.dtos.pedidos.ProdutoIdDTO;
import br.com.thallysprojetos.ecommerce.dtos.pedidos.UsuarioIdDTO;
import br.com.thallysprojetos.ecommerce.exceptions.pedidos.PedidosNotFoundException;
import br.com.thallysprojetos.ecommerce.exceptions.usuarios.UsuarioNotFoundException;
import br.com.thallysprojetos.ecommerce.models.Produtos;
import br.com.thallysprojetos.ecommerce.models.Usuarios;
import br.com.thallysprojetos.ecommerce.models.enums.StatusPagamento;
import br.com.thallysprojetos.ecommerce.models.enums.StatusPedidos;
import br.com.thallysprojetos.ecommerce.models.enums.TipoFormaPagamento;
import br.com.thallysprojetos.ecommerce.models.pagamentos.Pagamento;
import br.com.thallysprojetos.ecommerce.models.pedidos.ItemDoPedido;
import br.com.thallysprojetos.ecommerce.models.pedidos.Pedidos;
import br.com.thallysprojetos.ecommerce.repositories.PagamentoRepository;
import br.com.thallysprojetos.ecommerce.repositories.PedidosRepository;
import br.com.thallysprojetos.ecommerce.repositories.ProdutosRepository;
import br.com.thallysprojetos.ecommerce.repositories.UsuariosRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidosServiceTest {

    @Mock
    private PedidosRepository pedidosRepository;

    @Mock
    private UsuariosRepository usuarioRepository;

    @Mock
    private ProdutosRepository produtosRepository;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PedidosService pedidosService;

    private Pedidos pedido;
    private PedidosDTO pedidoDTO;
    private Usuarios usuario;
    private Produtos produto;
    private ItemDoPedido itemDoPedido;
    private ItemDoPedidoDTO itemDoPedidoDTO;
    private Pagamento pagamento;
    private PagamentoDTO pagamentoDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuarios(1L, "user", "email@test.com", "pass", new ArrayList<>());
        produto = new Produtos(1L, "Produto A", "Tipo", "Descrição", 100.0, true, 10);
        itemDoPedido = new ItemDoPedido(1L, 2, "Descrição Item", produto, new Pedidos());
        pagamento = new Pagamento(1L, 200.0, StatusPagamento.CRIADO, null, "PIX_KEY", null, null, null, null, null, pedido);
        pedido = new Pedidos(1L, LocalDateTime.now(), StatusPedidos.CRIADO, Collections.singletonList(itemDoPedido), usuario, pagamento);
        itemDoPedido.setPedido(pedido);

        UsuarioIdDTO usuarioIdDTO = new UsuarioIdDTO(1L);
        ProdutoIdDTO produtoIdDTO = new ProdutoIdDTO(1L);

        ProdutosDTO produtoDTO = new ProdutosDTO(1L, "Produto A", "Tipo", "Descrição", 100.0, true, 10);

        itemDoPedidoDTO = new ItemDoPedidoDTO(null, 2, "Descrição Item", produtoIdDTO);

        List<ItemDoPedidoDTO> itensDto = new ArrayList<>();
        itensDto.add(itemDoPedidoDTO);

        pedidoDTO = new PedidosDTO(null, LocalDateTime.now(), StatusPedidos.CRIADO, itensDto, usuarioIdDTO, null);

        pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setValor(200.0);
        pagamentoDTO.setTipoPagamento(TipoFormaPagamento.PIX);
        pagamentoDTO.setChavePix("PIX_KEY_TESTE");
    }

    @Test
    @DisplayName("Deve retornar uma página de pedidos ao chamar findAll")
    void findAll_ShouldReturnPageOfPedidosDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pedidos> page = new PageImpl<>(Collections.singletonList(pedido));

        when(pedidosRepository.findAll(pageable)).thenReturn(page);
        when(modelMapper.map(any(Pedidos.class), eq(PedidosDTO.class))).thenReturn(pedidoDTO);

        Page<PedidosDTO> result = pedidosService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(pedidosRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve encontrar um pedido por ID e retornar PedidosDTO")
    void findById_ShouldReturnPedidosDTO_WhenPedidoExists() {
        when(pedidosRepository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(modelMapper.map(any(Pedidos.class), eq(PedidosDTO.class))).thenReturn(pedidoDTO);

        PedidosDTO result = pedidosService.findById(1L);

        assertNotNull(result);
        assertEquals(pedidoDTO.getId(), result.getId());
        verify(pedidosRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar pedido por ID que não existe")
    void findById_ShouldThrowException_WhenPedidoDoesNotExist() {
        when(pedidosRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PedidosNotFoundException.class, () -> pedidosService.findById(1L));
        verify(pedidosRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve encontrar pedidos por ID de usuário e retornar uma lista de DTOs")
    void findByUserId_ShouldReturnListOfPedidosDTO_WhenPedidosExist() {
        List<Pedidos> pedidosList = Collections.singletonList(pedido);
        when(pedidosRepository.findByUsuarioId(anyLong())).thenReturn(pedidosList);
        when(modelMapper.map(any(Pedidos.class), eq(PedidosDTO.class))).thenReturn(pedidoDTO);

        List<PedidosDTO> result = pedidosService.findByUserId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(pedidosRepository, times(1)).findByUsuarioId(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar pedidos por ID de usuário que não existem")
    void findByUserId_ShouldThrowException_WhenNoPedidosExist() {
        when(pedidosRepository.findByUsuarioId(anyLong())).thenReturn(Collections.emptyList());

        assertThrows(PedidosNotFoundException.class, () -> pedidosService.findByUserId(1L));
        verify(pedidosRepository, times(1)).findByUsuarioId(1L);
    }

    @Test
    @DisplayName("Deve criar um novo pedido com sucesso")
    void createPedido_ShouldCreateAndReturnPedidosDTO() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(produtosRepository.findById(anyLong())).thenReturn(Optional.of(produto));
        when(pedidosRepository.save(any(Pedidos.class))).thenReturn(pedido);

        when(modelMapper.map(any(PedidosDTO.class), eq(Pedidos.class))).thenReturn(pedido);

        when(modelMapper.map(any(Pedidos.class), eq(PedidosDTO.class))).thenReturn(pedidoDTO);

        PedidosDTO result = pedidosService.createPedido(pedidoDTO);

        assertNotNull(result);
        assertEquals(pedidoDTO.getId(), result.getId());

        verify(usuarioRepository, times(1)).findById(anyLong());
        verify(produtosRepository, times(1)).findById(anyLong());
        verify(pedidosRepository, times(1)).save(any(Pedidos.class));

        verify(modelMapper, times(1)).map(any(PedidosDTO.class), eq(Pedidos.class));
        verify(modelMapper, times(1)).map(any(Pedidos.class), eq(PedidosDTO.class));

        verify(modelMapper, never()).map(any(ItemDoPedidoDTO.class), eq(ItemDoPedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar pedido com usuário inexistente")
    void createPedido_ShouldThrowException_WhenUsuarioDoesNotExist() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> pedidosService.createPedido(pedidoDTO));
        verify(usuarioRepository, times(1)).findById(1L);
        verify(pedidosRepository, never()).save(any(Pedidos.class));
    }

    @Test
    @DisplayName("Deve processar o pagamento de um pedido com sucesso")
    void processarPagamentoDoPedido_ShouldUpdatePedidoAndPagamento() {
        when(pedidosRepository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);
        when(modelMapper.map(any(PagamentoDTO.class), eq(Pagamento.class))).thenReturn(pagamento);

        assertDoesNotThrow(() -> pedidosService.processarPagamentoDoPedido(1L, pagamentoDTO));

        verify(pedidosRepository, times(1)).findById(1L);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
        verify(pedidosRepository, times(1)).save(any(Pedidos.class));

        assertEquals(StatusPedidos.AGUARDANDO_CONFIRMACAO, pedido.getStatusPedidos());
    }

    @Test
    @DisplayName("Deve lançar exceção ao processar pagamento de pedido inexistente")
    void processarPagamentoDoPedido_ShouldThrowException_WhenPedidoDoesNotExist() {
        when(pedidosRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PedidosNotFoundException.class, () -> pedidosService.processarPagamentoDoPedido(1L, pagamentoDTO));
        verify(pedidosRepository, times(1)).findById(1L);
        verify(pagamentoRepository, never()).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Deve atualizar o status de um pedido com sucesso")
    void updatePedidos_ShouldUpdatePedidoStatus_WhenPedidoExists() {
        when(pedidosRepository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(pedidosRepository.save(any(Pedidos.class))).thenReturn(pedido);
        when(modelMapper.map(any(Pedidos.class), eq(PedidosDTO.class))).thenReturn(pedidoDTO);

        PedidosDTO updatedDto = new PedidosDTO(1L, null, StatusPedidos.CANCELADO, null, null, null);
        updatedDto.setUsuario(new UsuarioIdDTO(1L)); // Corrected here

        PedidosDTO result = pedidosService.updatePedidos(1L, updatedDto);

        assertNotNull(result);
        assertEquals(StatusPedidos.CANCELADO, pedido.getStatusPedidos());
        verify(pedidosRepository, times(1)).findById(1L);
        verify(pedidosRepository, times(1)).save(any(Pedidos.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar pedido que não existe")
    void updatePedidos_ShouldThrowException_WhenPedidoDoesNotExist() {
        when(pedidosRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PedidosNotFoundException.class, () -> pedidosService.updatePedidos(1L, pedidoDTO));
        verify(pedidosRepository, times(1)).findById(1L);
        verify(pedidosRepository, never()).save(any(Pedidos.class));
    }

    @Test
    @DisplayName("Deve deletar um pedido com sucesso")
    void deletePedidos_ShouldDeletePedido_WhenPedidoExists() {
        when(pedidosRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(pedidosRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> pedidosService.deletePedidos(1L));
        verify(pedidosRepository, times(1)).existsById(1L);
        verify(pedidosRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar pedido que não existe")
    void deletePedidos_ShouldThrowException_WhenPedidoDoesNotExist() {
        when(pedidosRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(PedidosNotFoundException.class, () -> pedidosService.deletePedidos(1L));
        verify(pedidosRepository, times(1)).existsById(1L);
        verify(pedidosRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve confirmar um pedido com sucesso")
    void confirmarPedidos_ShouldUpdateStatusToPago_WhenPagamentoIsConfirmed() {
        pagamento.setStatus(StatusPagamento.CONFIRMADO);
        when(pedidosRepository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(pedidosRepository.save(any(Pedidos.class))).thenReturn(pedido);

        pedidosService.confirmarPedidos(1L);

        assertEquals(StatusPedidos.PAGO, pedido.getStatusPedidos());
        verify(pedidosRepository, times(1)).findById(1L);
        verify(pedidosRepository, times(1)).save(any(Pedidos.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao confirmar pedido sem pagamento")
    void confirmarPedidos_ShouldThrowException_WhenPagamentoIsNull() {
        pedido.setPagamento(null);
        when(pedidosRepository.findById(anyLong())).thenReturn(Optional.of(pedido));

        assertThrows(PedidosNotFoundException.class, () -> pedidosService.confirmarPedidos(1L));
        verify(pedidosRepository, times(1)).findById(1L);
        verify(pedidosRepository, never()).save(any(Pedidos.class));
    }

    @Test
    @DisplayName("Deve cancelar um pedido com sucesso")
    void cancelarPedido_ShouldUpdateStatusToCancelado() {
        when(pedidosRepository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(pedidosRepository.save(any(Pedidos.class))).thenReturn(pedido);

        pedidosService.cancelarPedido(1L);

        assertEquals(StatusPedidos.CANCELADO, pedido.getStatusPedidos());
        verify(pedidosRepository, times(1)).findById(1L);
        verify(pedidosRepository, times(1)).save(any(Pedidos.class));
    }

}