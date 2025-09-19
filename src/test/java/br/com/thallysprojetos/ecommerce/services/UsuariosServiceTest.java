package br.com.thallysprojetos.ecommerce.services;

import br.com.thallysprojetos.ecommerce.dtos.UsuariosDTO;
import br.com.thallysprojetos.ecommerce.exceptions.usuarios.UsuarioNotFoundException;
import br.com.thallysprojetos.ecommerce.models.Usuarios;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuariosServiceTest {

    @Mock
    private UsuariosRepository usuariosRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UsuariosService usuariosService;

    private Usuarios usuario;
    private UsuariosDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuarios();
        usuario.setId(1L);
        usuario.setEmail("test@email.com");
        usuario.setUserName("testuser");
        usuario.setPassword("password");

        usuarioDTO = new UsuariosDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setEmail("test@email.com");
        usuarioDTO.setUserName("testuser");
    }

    @Test
    @DisplayName("Deve retornar uma página de usuários ao chamar findAll")
    void findAll_ShouldReturnPageOfUsuariosDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuarios> page = new PageImpl<>(Collections.singletonList(usuario));

        when(usuariosRepository.findAll(pageable)).thenReturn(page);
        when(modelMapper.map(any(Usuarios.class), eq(UsuariosDTO.class))).thenReturn(usuarioDTO);

        Page<UsuariosDTO> result = usuariosService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(usuariosRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve encontrar um usuário pelo ID e retornar UsuariosDTO")
    void findById_ShouldReturnUsuariosDTO_WhenUsuarioExists() {
        when(usuariosRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(modelMapper.map(any(Usuarios.class), eq(UsuariosDTO.class))).thenReturn(usuarioDTO);

        UsuariosDTO result = usuariosService.findById(1L);

        assertNotNull(result);
        assertEquals(usuarioDTO.getId(), result.getId());
        verify(usuariosRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário por ID que não existe")
    void findById_ShouldThrowException_WhenUsuarioDoesNotExist() {
        when(usuariosRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> usuariosService.findById(1L));
        verify(usuariosRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve encontrar usuário por e-mail e retornar UsuariosDTO")
    void findByEmail_ShouldReturnUsuariosDTO_WhenUsuarioExists() {
        when(usuariosRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
        when(modelMapper.map(any(Usuarios.class), eq(UsuariosDTO.class))).thenReturn(usuarioDTO);

        UsuariosDTO result = usuariosService.findByEmail("test@email.com");

        assertNotNull(result);
        assertEquals(usuarioDTO.getEmail(), result.getEmail());
        verify(usuariosRepository, times(1)).findByEmail("test@email.com");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário por e-mail que não existe")
    void findByEmail_ShouldThrowException_WhenUsuarioDoesNotExist() {
        when(usuariosRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> usuariosService.findByEmail("nonexistent@email.com"));
        verify(usuariosRepository, times(1)).findByEmail("nonexistent@email.com");
    }

    @Test
    @DisplayName("Deve criar um novo usuário e retornar UsuariosDTO")
    void createUser_ShouldCreateAndReturnUsuariosDTO() {
        when(modelMapper.map(any(UsuariosDTO.class), eq(Usuarios.class))).thenReturn(usuario);
        when(usuariosRepository.save(any(Usuarios.class))).thenReturn(usuario);
        when(modelMapper.map(any(Usuarios.class), eq(UsuariosDTO.class))).thenReturn(usuarioDTO);

        UsuariosDTO result = usuariosService.createUser(usuarioDTO);

        assertNotNull(result);
        assertEquals(usuarioDTO.getId(), result.getId());
        verify(usuariosRepository, times(1)).save(any(Usuarios.class));
    }

    @Test
    @DisplayName("Deve atualizar um usuário com sucesso")
    void updateUsuarios_ShouldUpdateUsuario_WhenUsuarioExists() {
        when(usuariosRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(usuariosRepository.save(any(Usuarios.class))).thenReturn(usuario);

        UsuariosDTO updatedDto = new UsuariosDTO();
        updatedDto.setId(1L);
        updatedDto.setUserName("updateduser");
        updatedDto.setEmail("updated@email.com");

        usuariosService.updateUsuarios(1L, updatedDto);

        verify(usuariosRepository, times(1)).findById(1L);
        verify(usuariosRepository, times(1)).save(any(Usuarios.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar usuário que não existe")
    void updateUsuarios_ShouldThrowException_WhenUsuarioDoesNotExist() {
        when(usuariosRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> usuariosService.updateUsuarios(1L, usuarioDTO));
        verify(usuariosRepository, times(1)).findById(1L);
        verify(usuariosRepository, never()).save(any(Usuarios.class));
    }

    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    void deleteUsuarios_ShouldDeleteUsuario_WhenUsuarioExists() {
        when(usuariosRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(usuariosRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> usuariosService.deleteUsuarios(1L));
        verify(usuariosRepository, times(1)).existsById(1L);
        verify(usuariosRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar usuário que não existe")
    void deleteUsuarios_ShouldThrowException_WhenUsuarioDoesNotExist() {
        when(usuariosRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UsuarioNotFoundException.class, () -> usuariosService.deleteUsuarios(1L));
        verify(usuariosRepository, times(1)).existsById(1L);
        verify(usuariosRepository, never()).deleteById(anyLong());
    }

}