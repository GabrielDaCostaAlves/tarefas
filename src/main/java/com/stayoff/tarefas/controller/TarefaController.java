package com.stayoff.tarefas.controller;

import com.stayoff.tarefas.dto.entrada.TarefaDto;
import com.stayoff.tarefas.dto.saida.TarefaResponseDTO;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.repository.UsuarioRepository;
import com.stayoff.tarefas.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;
    private final UsuarioRepository usuarioRepository;

    public TarefaController (TarefaService tarefaService, UsuarioRepository usuarioRepository){
        this.tarefaService = tarefaService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> criarTarefa(@Valid @RequestBody TarefaDto tarefaDto){

        Usuario usuario =  usuarioRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado."));

        TarefaResponseDTO tarefaResponseDTO = tarefaService.criarTarefa(tarefaDto, usuario);

        return ResponseEntity.ok(tarefaResponseDTO);
    }
}
