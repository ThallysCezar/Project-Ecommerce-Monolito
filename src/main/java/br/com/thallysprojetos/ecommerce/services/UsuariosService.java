package br.com.thallysprojetos.ecommerce.services;


import br.com.thallysprojetos.ecommerce.dtos.UsuariosDTO;
import br.com.thallysprojetos.ecommerce.models.Usuarios;
import br.com.thallysprojetos.ecommerce.repositories.UsuariosRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;
    private final ModelMapper modelMapper;

    public Page<UsuariosDTO> findAll(Pageable page) {
        return usuariosRepository.findAll(page).map(p -> modelMapper.map(p, UsuariosDTO.class));
    }

    public UsuariosDTO findById(Long id) {
        return usuariosRepository.findById(id)
                .map(p -> modelMapper.map(p, UsuariosDTO.class))
                .orElseThrow(NoSuchElementException::new);
    }

    public UsuariosDTO findByEmail(String email) {
        return usuariosRepository.findByEmail(email)
                .map(u -> modelMapper.map(u, UsuariosDTO.class))
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com o e-mail fornecido."));
    }

    public UsuariosDTO createUser(UsuariosDTO dto) {

            Usuarios usuarios = modelMapper.map(dto, Usuarios.class);

            Usuarios usuariosSaved = usuariosRepository.save(usuarios);

            return modelMapper.map(usuariosSaved, UsuariosDTO.class);
    }

    public void updateUsuarios(Long id, UsuariosDTO dto) {
        try {
            Usuarios usuarios = modelMapper.map(dto, Usuarios.class);
            usuarios.setId(id);
            usuarios = usuariosRepository.save(usuarios);

            modelMapper.map(usuarios, UsuariosDTO.class);
        } catch (Exception exUser) {
            throw new NoSuchElementException("Usuarios não encontrado.");
        }
    }

    public void deleteUsuarios(Long id) {
        if (!usuariosRepository.existsById(id)) {
            throw new NoSuchElementException(String.format("Usuarios não encontrado com o id '%s'.", id));
        }
        usuariosRepository.deleteById(id);
    }

}