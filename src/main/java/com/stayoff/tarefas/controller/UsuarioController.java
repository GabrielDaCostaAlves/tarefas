package com.stayoff.tarefas.controller;

import com.stayoff.tarefas.dto.entrada.UsuarioDto;
import com.stayoff.tarefas.dto.saida.UsuarioResponseDTO;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }


    @PutMapping
    public ResponseEntity<UsuarioResponseDTO> atualizaUsuario(@Valid @RequestBody UsuarioDto usuarioDto){

        Usuario usuarioLogado = usuarioService.getUsuarioLogado();


        UsuarioResponseDTO usuarioResponseDTO = usuarioService.atualizaUsuario(usuarioDto, usuarioLogado);

        return ResponseEntity.ok(usuarioResponseDTO);
    }


    @DeleteMapping
    public ResponseEntity<String> deleteUsuario(){

        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        usuarioService.excluirUsuarioLogado();

        return ResponseEntity.ok("Usuário excluído com sucesso!");
    }
}
