package com.stayoff.tarefas.controller;

import com.stayoff.tarefas.dto.entrada.CadastroRequestDTO;
import com.stayoff.tarefas.dto.entrada.LoginRequestDTO;
import com.stayoff.tarefas.dto.saida.AuthResponseDTO;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.repository.UsuarioRepository;
import com.stayoff.tarefas.security.jwt.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil,
                          UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        String token = jwtUtil.gerarToken(request.email());


        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(()-> new IllegalArgumentException("Usuário não encontrado."));


        return new AuthResponseDTO(
                token,
                usuario.getId()
        );
    }

    @PostMapping("/cadastro")
    public AuthResponseDTO cadastrar(@RequestBody CadastroRequestDTO request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .build();
        usuarioRepository.save(usuario);


        String token = jwtUtil.gerarToken(usuario.getEmail());
        return new AuthResponseDTO(
                token,
                usuario.getId());
    }
}
