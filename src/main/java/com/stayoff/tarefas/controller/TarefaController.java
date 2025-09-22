package com.stayoff.tarefas.controller;

import com.stayoff.tarefas.dto.entrada.TarefaDto;
import com.stayoff.tarefas.dto.entrada.TarefaUpdateDTO;
import com.stayoff.tarefas.dto.paginado.PagedResponseDTO;
import com.stayoff.tarefas.dto.saida.TarefaResponseDTO;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.service.TarefaService;
import com.stayoff.tarefas.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

    @Operation(summary = "Criar nova tarefa", description = "Cria uma nova tarefa para o usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa criada com sucesso",
                    content = @Content(schema = @Schema(implementation = TarefaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TarefaResponseDTO> criarTarefa(@Valid @RequestBody TarefaDto tarefaDto){
        Usuario usuario = usuarioService.getUsuarioLogado();
        TarefaResponseDTO tarefaResponseDTO = tarefaService.criarTarefa(tarefaDto, usuario);
        return ResponseEntity.ok(tarefaResponseDTO);
    }

    @Operation(summary = "Atualizar tarefa", description = "Atualiza os dados de uma tarefa existente do usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = TarefaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PutMapping("/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> atualizarTarefa(
            @Valid @RequestBody TarefaUpdateDTO tarefaUpdateDTO,
            @PathVariable Long idTarefa){
        Usuario usuario = usuarioService.getUsuarioLogado();
        TarefaResponseDTO tarefaResponseDTO = tarefaService.atualizarTarefa(idTarefa, tarefaUpdateDTO, usuario);
        return ResponseEntity.ok(tarefaResponseDTO);
    }

    @Operation(summary = "Atualizar status de conclusão da tarefa", description = "Marca ou desmarca uma tarefa como concluída.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    @PutMapping("/{idTarefa}/concluido/{verificacao}")
    public ResponseEntity<String> atualizarTarefaConcluido(
            @PathVariable Long idTarefa,
            @PathVariable Long verificacao){
        Usuario usuario = usuarioService.getUsuarioLogado();
        tarefaService.atualizarTarefaConcluido(idTarefa, verificacao, usuario);
        return ResponseEntity.ok("Alterado com sucesso!");
    }

    @Operation(summary = "Excluir tarefa", description = "Exclui uma tarefa existente do usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa excluída com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    @DeleteMapping("/{idTarefa}")
    public ResponseEntity<String> excluiTarefa(@PathVariable Long idTarefa){
        Usuario usuario = usuarioService.getUsuarioLogado();
        tarefaService.excluirTarefa(idTarefa, usuario);
        return ResponseEntity.ok("Tarefa excluida com sucesso!");
    }

    @Operation(summary = "Listar tarefas paginadas", description = "Retorna todas as tarefas do usuário logado, paginadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TarefaResponseDTO.class))))
    })
    @GetMapping
    public ResponseEntity<PagedResponseDTO<TarefaResponseDTO>> buscaTodasTarefas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Usuario usuario = usuarioService.getUsuarioLogado();
        Page<TarefaResponseDTO> tarefasPaginado = tarefaService.buscaTarefasPaginado(usuario, page, size);
        return ResponseEntity.ok(PagedResponseDTO.from(tarefasPaginado));
    }
}
