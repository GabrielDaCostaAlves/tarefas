package com.stayoff.tarefas.dto.entrada;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioAtualizarDto(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String nome,

        @Email
        @NotBlank(message = "O e-mail é obrigatório.")
        @Size(min = 5, max = 100, message = "O e-mail deve ter entre 5 e 100 caracteres.")
        String email,


        @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres.")
        String senha
) {
}
