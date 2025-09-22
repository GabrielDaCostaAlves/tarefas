package com.stayoff.tarefas.controller;

import com.stayoff.tarefas.dto.entrada.TarefaDto;
import com.stayoff.tarefas.dto.entrada.TarefaUpdateDTO;
import com.stayoff.tarefas.dto.paginado.PagedResponseDTO;
import com.stayoff.tarefas.dto.saida.TarefaResponseDTO;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.service.TarefaService;
import com.stayoff.tarefas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;
    private final UsuarioService usuarioService;

    public TarefaController(TarefaService tarefaService, UsuarioService usuarioService){
        this.tarefaService = tarefaService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> criarTarefa(@Valid @RequestBody TarefaDto tarefaDto){
        Usuario usuario = usuarioService.getUsuarioLogado();
        TarefaResponseDTO tarefaResponseDTO = tarefaService.criarTarefa(tarefaDto, usuario);
        return ResponseEntity.ok(tarefaResponseDTO);
    }

    @PutMapping("/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> atualizarTarefa(@Valid @RequestBody TarefaUpdateDTO tarefaUpdateDTO, @PathVariable Long idTarefa){
        Usuario usuario = usuarioService.getUsuarioLogado();
        TarefaResponseDTO tarefaResponseDTO = tarefaService.atualizarTarefa(idTarefa, tarefaUpdateDTO, usuario);
        return ResponseEntity.ok(tarefaResponseDTO);
    }

    @PutMapping("/{idTarefa}/concluido/{verificacao}")
    public ResponseEntity<String> atualizarTarefaConcluido(@PathVariable Long idTarefa, @PathVariable Long verificacao){
        Usuario usuario = usuarioService.getUsuarioLogado();
        tarefaService.atualizarTarefaConcluido(idTarefa, verificacao, usuario);
        return ResponseEntity.ok("Alterado com sucesso!");
    }

    @DeleteMapping("/{idTarefa}")
    public ResponseEntity<String> excluiTarefa(@PathVariable Long idTarefa){
        Usuario usuario = usuarioService.getUsuarioLogado();
        tarefaService.excluirTarefa(idTarefa, usuario);
        return ResponseEntity.ok("Tarefa excluida com sucesso!");
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<TarefaResponseDTO>> buscaTodasTarefas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Usuario usuario = usuarioService.getUsuarioLogado();
        Page<TarefaResponseDTO> tarefasPaginado = tarefaService.buscaTarefasPaginado(usuario, page, size);
        return ResponseEntity.ok(PagedResponseDTO.from(tarefasPaginado));
    }
}
