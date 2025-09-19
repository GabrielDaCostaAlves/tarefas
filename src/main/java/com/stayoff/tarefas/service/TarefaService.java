package com.stayoff.tarefas.service;

import com.stayoff.tarefas.dto.entrada.TarefaDto;
import com.stayoff.tarefas.dto.entrada.TarefaUpdateDTO;
import com.stayoff.tarefas.dto.saida.TarefaResponseDTO;
import com.stayoff.tarefas.model.Tarefa;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    @Transactional
    public TarefaResponseDTO criarTarefa(TarefaDto tarefaDto, Usuario usuario){
        Tarefa tarefa = Tarefa.builder()
                .titulo(tarefaDto.titulo())
                .descricao(tarefaDto.descricao())
                .usuario(usuario)
                .build();
        tarefa = tarefaRepository.save(tarefa);
        return new TarefaResponseDTO(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getConcluido(), tarefa.getDataCriacao());
    }

    @Transactional
    public TarefaResponseDTO atualizarTarefa(Long idTarefa, TarefaUpdateDTO tarefaUpdateDTO, Usuario usuario){
        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada."));

        if (!tarefa.getUsuario().getId().equals(usuario.getId())){
            throw new SecurityException("Você não pode alterar essa tarefa.");
        }

        tarefa.setTitulo(tarefaUpdateDTO.titulo());
        tarefa.setDescricao(tarefaUpdateDTO.descricao());
        tarefa.setConcluido(tarefaUpdateDTO.concluido());
        tarefa = tarefaRepository.save(tarefa);

        return new TarefaResponseDTO(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getConcluido(), tarefa.getDataCriacao());
    }

    @Transactional
    public void excluirTarefa(Long id, Usuario usuario){
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não localizada."));

        if (!tarefa.getUsuario().getId().equals(usuario.getId())){
            throw new SecurityException("Você não pode excluir essa tarefa.");
        }

        tarefaRepository.delete(tarefa);
    }

    public Page<TarefaResponseDTO> buscaTarefasPaginado(Usuario usuario, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return tarefaRepository.findByUsuario(usuario, pageable)
                .map(t -> new TarefaResponseDTO(t.getId(), t.getTitulo(), t.getDescricao(), t.getConcluido(), t.getDataCriacao()));
    }

    @Transactional
    public void atualizarTarefaConcluido(Long idTarefa, Long verificacao, Usuario usuario){
        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada."));

        if (!tarefa.getUsuario().getId().equals(usuario.getId())){
            throw new SecurityException("Você não pode alterar essa tarefa.");
        }

        if (verificacao != 0 && verificacao != 1) {
            throw new IllegalArgumentException("Concluido precisa receber 1 ou 0.");
        }

        tarefa.setConcluido(verificacao == 1);
        tarefaRepository.save(tarefa);
    }
}
