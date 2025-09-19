package com.stayoff.tarefas.service;

import com.stayoff.tarefas.dto.entrada.UsuarioDto;
import com.stayoff.tarefas.dto.saida.UsuarioResponseDTO;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioDto usuarioDto){
        validarEmail(usuarioDto.email(), null);

        Usuario usuario = Usuario.builder()
                .nome(usuarioDto.nome())
                .email(usuarioDto.email())
                .senha(passwordEncoder.encode(usuarioDto.senha()))
                .build();

        usuario = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }

    @Transactional
    private void validarEmail(String email, Long usuarioId) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }

        Usuario usuarioExistente = usuarioRepository.findByEmail(email).orElse(null);

        if (usuarioExistente != null && !usuarioExistente.getId().equals(usuarioId)) {
            throw new IllegalArgumentException("E-mail já utilizado por outro usuário!");
        }
    }

    @Transactional
    public UsuarioResponseDTO atualizaUsuario(UsuarioDto usuarioDto, Usuario usuario){
        validarEmail(usuarioDto.email(), usuario.getId());

        usuario.setNome(usuarioDto.nome());
        usuario.setEmail(usuarioDto.email());
        usuario.setSenha(passwordEncoder.encode(usuarioDto.senha()));

        usuario = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }

    @Transactional
    public void excluirUsuarioLogado(){
        Usuario usuarioLogado = getUsuarioLogado();
        usuarioRepository.delete(usuarioLogado);
    }

    @Transactional
    public Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }
}
