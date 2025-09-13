package com.stayoff.tarefas.dto.saida;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;


public record TarefaResponseDTO(

        Long id,

        String titulo,

        String descricao,

        Boolean concluido,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime dataCriacao

) {
}
