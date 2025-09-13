package com.stayoff.tarefas.service;

import com.stayoff.tarefas.dto.entrada.TarefaDto;
import com.stayoff.tarefas.dto.paginado.PagedResponseDTO;
import com.stayoff.tarefas.dto.saida.TarefaResponseDTO;
import com.stayoff.tarefas.model.Tarefa;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.repository.TarefaRepository;
import org.springframework.stereotype.Service;

@Service
public class TarefaService {

    private TarefaRepository tarefaRepository;

    public TarefaService (TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public TarefaResponseDTO criarTarefa(TarefaDto tarefaDto, Usuario usuario){

        Tarefa tarefa = Tarefa.builder()
                .titulo(tarefaDto.titulo())
                .descricao(tarefaDto.descricao())
                .usuario(usuario)
                .build();

        tarefa= tarefaRepository.save(tarefa);


        return new TarefaResponseDTO(tarefa.getId(),tarefa.getTitulo(),tarefa.getDescricao(),tarefa.getConcluido(),tarefa.getDataCriacao());

    }

    public TarefaResponseDTO atualizarTarefa(TarefaDto tarefaDto, Usuario usuario){

        return null;
    }

    public void excluirTarefa(TarefaDto tarefaDto, Usuario usuario){


    }

    public PagedResponseDTO<TarefaResponseDTO> buscaTarefasPaginado(Usuario usuario){

        return null;
    }

}
