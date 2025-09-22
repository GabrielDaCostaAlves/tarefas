package com.stayoff.tarefas.dto.entrada;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record TarefaUpdateDTO(

        @Size(min = 3 , max = 100 ,message = "O titulo tem que estar entre 3 e 100 caracteres.")
        @NotBlank(message = "O titulo é obrigatório.")
        String titulo,

        @Size(max = 300 ,message = "A descrição deve ter no máximo 300 caracteres.")
        String descricao,

        @NotNull(message = "Estado do concluido é obrigatório.")
        Boolean concluido

) {
}
