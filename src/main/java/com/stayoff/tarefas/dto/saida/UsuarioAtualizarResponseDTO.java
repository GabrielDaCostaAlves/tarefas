package com.stayoff.tarefas.dto.saida;


public record UsuarioAtualizarResponseDTO(

        Long id,

        String nome,

        String email,

        String token
) {
}
