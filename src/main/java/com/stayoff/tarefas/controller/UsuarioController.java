package com.stayoff.tarefas.controller;

import com.stayoff.tarefas.dto.entrada.UsuarioDto;
import com.stayoff.tarefas.dto.saida.UsuarioResponseDTO;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.repository.UsuarioRepository;
import com.stayoff.tarefas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioService usuarioService ,UsuarioRepository usuarioRepository){
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        UsuarioResponseDTO usuarioResponse = usuarioService.criarUsuario(usuarioDto);
        return ResponseEntity.ok(usuarioResponse);
    }


    @PutMapping
    public  ResponseEntity<UsuarioResponseDTO> atualizaUsuario (@Valid @RequestBody UsuarioDto usuarioDto){

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(1L);
        Usuario usuario = new Usuario();
        if (usuarioOpt.isPresent()) {
            usuario = usuarioOpt.get();

        } else {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.atualizaUsuario(usuarioDto,usuario);

        return ResponseEntity.ok(usuarioResponseDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long id){


        usuarioService.excluirUsuario(id);


        return ResponseEntity.ok("Usuario excluido com sucesso!");
    }
}
