package com.stayoff.tarefas.repository;

import com.stayoff.tarefas.model.Tarefa;
import com.stayoff.tarefas.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa,Long> {

    Page<Tarefa> findByUsuario(Usuario usuario, Pageable pageable);

    long countByUsuarioId(Long id);
}
