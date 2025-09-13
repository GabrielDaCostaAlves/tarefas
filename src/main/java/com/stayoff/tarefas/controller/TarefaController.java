package com.stayoff.tarefas.controller;

import com.stayoff.tarefas.dto.entrada.TarefaDto;
import com.stayoff.tarefas.dto.entrada.TarefaUpdateDTO;
import com.stayoff.tarefas.dto.saida.TarefaResponseDTO;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.repository.UsuarioRepository;
import com.stayoff.tarefas.service.TarefaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado."));

        TarefaResponseDTO tarefaResponseDTO = tarefaService.criarTarefa(tarefaDto, usuario);

        return ResponseEntity.ok(tarefaResponseDTO);
    }


    @PutMapping("/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> atualizarTarefa(@Valid @RequestBody TarefaUpdateDTO tarefaUpdateDTO, @PathVariable Long idTarefa  ){

        Usuario usuario =  usuarioRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado."));

        TarefaResponseDTO tarefaResponseDTO = tarefaService.atualizarTarefa(idTarefa,tarefaUpdateDTO, usuario);

        return ResponseEntity.ok(tarefaResponseDTO);
    }


    @DeleteMapping("/{idTarefa}")
    public  ResponseEntity<String> excluiTarefa(@PathVariable Long idTarefa){

        Usuario usuario = usuarioRepository.findById(1L)
                .orElseThrow(()-> new EntityNotFoundException("Usuario não encontrado."));

        tarefaService.excluirTarefa(idTarefa,usuario);
        return  ResponseEntity.ok("Tarefa excluida com sucesso!");
    }


}
