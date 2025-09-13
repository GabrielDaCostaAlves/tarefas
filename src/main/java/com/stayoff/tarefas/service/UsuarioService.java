package com.stayoff.tarefas.service;

import com.stayoff.tarefas.dto.entrada.UsuarioDto;
import com.stayoff.tarefas.dto.saida.UsuarioResponseDTO;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponseDTO criarUsuario(UsuarioDto usuarioDto){

        validarEmail(usuarioDto);
        Usuario usuario = Usuario.builder()
                .nome(usuarioDto.nome())
                .email(usuarioDto.email())
                .senha(usuarioDto.senha())
                .build();

        usuario =  usuarioRepository.save(usuario);


        return new UsuarioResponseDTO(
                usuario.getId(),usuario.getNome(),usuario.getEmail()
        );
    }

    public void validarEmail(UsuarioDto usuarioDto) {
        Usuario usuario;
        if (usuarioDto.email() == null || usuarioDto.email().isBlank() || !usuarioDto.email().contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }

        usuario = usuarioRepository.findByEmail(usuarioDto.email());
        if (usuario != null) {
            throw new IllegalArgumentException("E-mail já utilizado para cadastro!");
        }

    }




}
