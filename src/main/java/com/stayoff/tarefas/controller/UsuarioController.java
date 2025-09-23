package com.stayoff.tarefas.controller;

import com.stayoff.tarefas.dto.entrada.UsuarioAtualizarDto;
import com.stayoff.tarefas.dto.saida.UsuarioAtualizarResponseDTO;
import com.stayoff.tarefas.dto.saida.UsuarioResponseDTO;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearerAuth") // JWT obrigatório
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Atualizar dados do usuário logado", description = "Atualiza o nome, email e senha do usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioAtualizarResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado, token ausente ou inválido", content = @Content)
    })
    @PutMapping
    public ResponseEntity<UsuarioAtualizarResponseDTO> atualizaUsuario(
            @Valid @RequestBody UsuarioAtualizarDto usuarioAtualizarDto){

        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        UsuarioAtualizarResponseDTO usuarioResponseDTO = usuarioService.atualizaUsuario(usuarioAtualizarDto, usuarioLogado);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @Operation(summary = "Excluir usuário logado", description = "Exclui permanentemente o usuário atualmente logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado, token ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @DeleteMapping
    public ResponseEntity<String> deleteUsuario(){
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        usuarioService.excluirUsuarioLogado();
        return ResponseEntity.ok("Usuário excluído com sucesso!");
    }

    @Operation(summary = "Buscar usuário logado", description = "Retorna os dados do usuário atualmente logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado, token ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<UsuarioResponseDTO> buscaUsuario(){
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO(
                usuarioLogado.getId(),
                usuarioLogado.getNome(),
                usuarioLogado.getEmail()
        );
        return ResponseEntity.ok(usuarioResponseDTO);
    }
}
