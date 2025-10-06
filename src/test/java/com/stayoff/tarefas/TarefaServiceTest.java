package com.stayoff.tarefas;

import com.stayoff.tarefas.dto.entrada.TarefaDto;
import com.stayoff.tarefas.dto.entrada.TarefaUpdateDTO;
import com.stayoff.tarefas.model.Tarefa;
import com.stayoff.tarefas.model.Usuario;
import com.stayoff.tarefas.repository.TarefaRepository;
import com.stayoff.tarefas.service.TarefaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    private Usuario usuario;


    @Test
    void deveCriarTarefaComSucesso() {

        usuario = Usuario.builder()
                .id(1L)
                .nome("Gabriel")
                .email("gabriel@email.com")
                .build();


        TarefaDto dto = new TarefaDto("Teste", "Descrição");
        when(tarefaRepository.countByUsuarioId(usuario.getId())).thenReturn(0L);



        Tarefa tarefaSalva = Tarefa.builder()
                .id(1L)
                .titulo(dto.titulo())
                .descricao(dto.descricao())
                .usuario(usuario)
                .dataCriacao(LocalDateTime.now())
                .concluido(false)
                .build();



        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaSalva);

        var response = tarefaService.criarTarefa(dto, usuario);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.titulo()).isEqualTo("Teste");
        assertThat(response.descricao()).isEqualTo("Descrição");

        ArgumentCaptor<Tarefa> captor = ArgumentCaptor.forClass(Tarefa.class);
        verify(tarefaRepository).save(captor.capture());
        assertThat(captor.getValue().getUsuario()).isEqualTo(usuario);
    }


    @Test
    void deveAtualizarTarefaComSucesso() {
        usuario = Usuario.builder().id(1L).nome("Gabriel").email("gabriel@email.com").build();

        Tarefa tarefaExistente = Tarefa.builder()
                .id(1L)
                .titulo("Antigo")
                .descricao("Antiga desc")
                .usuario(usuario)
                .concluido(false)
                .dataCriacao(LocalDateTime.now())
                .build();

        when(tarefaRepository.findById(1L)).thenReturn(java.util.Optional.of(tarefaExistente));
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(i -> i.getArguments()[0]);

        var updateDTO = new TarefaUpdateDTO("Novo título", "Nova desc", true);
        var response = tarefaService.atualizarTarefa(1L, updateDTO, usuario);

        assertThat(response.titulo()).isEqualTo("Novo título");
        assertThat(response.descricao()).isEqualTo("Nova desc");
        assertThat(response.concluido()).isTrue();
    }

    @Test
    void deveLancarErroAoAtualizarTarefaDeOutroUsuario() {

        Usuario outroUsuario = Usuario.builder().id(2L).nome("Outro").email("outro@email.com").build();
        usuario = Usuario.builder().id(1L).nome("Gabriel").email("gabriel@email.com").build();

        Tarefa tarefaExistente = Tarefa.builder().id(1L).usuario(usuario).build();

        when(tarefaRepository.findById(1L)).thenReturn(java.util.Optional.of(tarefaExistente));

        var updateDTO = new TarefaUpdateDTO("Novo", "Nova", true);
        assertThrows(SecurityException.class, () -> tarefaService.atualizarTarefa(1L, updateDTO, outroUsuario));
    }

    @Test
    void deveExcluirTarefaComSucesso() {
        usuario = Usuario.builder().id(1L).build();
        Tarefa tarefa = Tarefa.builder().id(1L).usuario(usuario).build();

        when(tarefaRepository.findById(1L)).thenReturn(java.util.Optional.of(tarefa));

        tarefaService.excluirTarefa(1L, usuario);

        verify(tarefaRepository).delete(tarefa);
    }

    @Test
    void deveLancarErroAoExcluirTarefaDeOutroUsuario() {
        usuario = Usuario.builder().id(1L).build();
        Usuario outroUsuario = Usuario.builder().id(2L).build();
        Tarefa tarefa = Tarefa.builder().id(1L).usuario(usuario).build();

        when(tarefaRepository.findById(1L)).thenReturn(java.util.Optional.of(tarefa));

        assertThrows(SecurityException.class, () -> tarefaService.excluirTarefa(1L, outroUsuario));
    }

    @Test
    void deveAtualizarStatusConcluidoComSucesso() {
        usuario = Usuario.builder().id(1L).build();
        Tarefa tarefa = Tarefa.builder().id(1L).usuario(usuario).concluido(false).build();

        when(tarefaRepository.findById(1L)).thenReturn(java.util.Optional.of(tarefa));

        tarefaService.atualizarTarefaConcluido(1L, 1L, usuario);

        assertThat(tarefa.getConcluido()).isTrue();
    }

    @Test
    void deveLancarErroAoAtualizarStatusConcluidoComValorInvalido() {
        usuario = Usuario.builder().id(1L).build();
        Tarefa tarefa = Tarefa.builder().id(1L).usuario(usuario).concluido(false).build();

        when(tarefaRepository.findById(1L)).thenReturn(java.util.Optional.of(tarefa));

        assertThrows(IllegalArgumentException.class, () -> tarefaService.atualizarTarefaConcluido(1L, 2L, usuario));
    }

    @Test
    void deveLancarErroAoCriarMaisDe30Tarefas() {
        usuario = Usuario.builder().id(1L).build();

        when(tarefaRepository.countByUsuarioId(usuario.getId())).thenReturn(30L);
        TarefaDto dto = new TarefaDto("Teste", "Descrição");

        assertThrows(IllegalArgumentException.class, () -> tarefaService.criarTarefa(dto, usuario));
    }



}
