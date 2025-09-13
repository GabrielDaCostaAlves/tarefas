package com.stayoff.tarefas.repository;

import com.stayoff.tarefas.dto.entrada.UsuarioDto;
import com.stayoff.tarefas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Usuario findByEmail(String email);
}
