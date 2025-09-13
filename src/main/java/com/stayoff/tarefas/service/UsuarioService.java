package com.stayoff.tarefas.service;

import com.stayoff.tarefas.dto.entrada.UsuarioDto;
import com.stayoff.tarefas.dto.saida.UsuarioResponseDTO;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponseDTO criarUsuario(UsuarioDto usuarioDto){

        validarEmail(usuarioDto.email(),null);
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

    private void validarEmail(String email, Long usuarioId) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }

        Usuario usuarioExistente = usuarioRepository.findByEmail(email);


        if (usuarioExistente != null && !usuarioExistente.getId().equals(usuarioId)) {
            throw new IllegalArgumentException("E-mail já utilizado por outro usuário!");
        }
    }

    public UsuarioResponseDTO atualizaUsuario(UsuarioDto usuarioDto,Usuario usuario){

        validarEmail(usuarioDto.email(),usuario.getId());



        usuario.setSenha(usuarioDto.senha());
        usuario.setEmail(usuarioDto.email());
        usuario.setNome(usuarioDto.nome());


        usuario =  usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
                usuario.getId(),usuario.getNome(),usuario.getEmail()
        );

    }


    public void excluirUsuario(Long id){

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        usuarioRepository.delete(usuario);


    }


}
