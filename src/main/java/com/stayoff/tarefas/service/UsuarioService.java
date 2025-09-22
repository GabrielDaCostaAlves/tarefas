package com.stayoff.tarefas.service;

import com.stayoff.tarefas.dto.entrada.UsuarioAtualizarDto;
import com.stayoff.tarefas.dto.entrada.UsuarioDto;
import com.stayoff.tarefas.dto.saida.UsuarioAtualizarResponseDTO;
import com.stayoff.tarefas.dto.saida.UsuarioResponseDTO;
import com.stayoff.tarefas.exception.ResourceNotFoundException;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.repository.UsuarioRepository;
import com.stayoff.tarefas.security.jwt.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
    public UsuarioAtualizarResponseDTO atualizaUsuario(UsuarioAtualizarDto usuarioAtualizarDto, Usuario usuario){

        boolean emailAlterado = !usuario.getEmail().equals(usuarioAtualizarDto.email());

        validarEmail(usuarioAtualizarDto.email(), usuario.getId());

        usuario.setNome(usuarioAtualizarDto.nome());
        usuario.setEmail(usuarioAtualizarDto.email());
        if (usuarioAtualizarDto.senha() != null) {
            usuario.setSenha(passwordEncoder.encode(usuarioAtualizarDto.senha()));
        }
        usuario = usuarioRepository.save(usuario);

        String novoToken = null;
        if (emailAlterado) {
            novoToken = jwtUtil.gerarToken(usuario.getEmail());
        }

        return new UsuarioAtualizarResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                novoToken
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
            throw new IllegalArgumentException("Usuário não autenticado");
        }
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }
}
