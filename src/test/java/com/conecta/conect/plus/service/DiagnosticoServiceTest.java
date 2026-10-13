package com.conecta.conect.plus.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.conecta.conect.plus.dto.DiagnosticoResponseDTO;
import com.conecta.conect.plus.entity.Competencia;
import com.conecta.conect.plus.entity.Curso;
import com.conecta.conect.plus.entity.CursoCompetencia;
import com.conecta.conect.plus.entity.UsuarioCompetencia;
import com.conecta.conect.plus.entity.VagaCompetencia;
import com.conecta.conect.plus.repository.CompetenciaRepository;
import com.conecta.conect.plus.repository.CursoCompetenciaRepository;
import com.conecta.conect.plus.repository.CursoRepository;
import com.conecta.conect.plus.repository.UsuarioCompetenciaRepository;
import com.conecta.conect.plus.repository.UsuarioRepository;
import com.conecta.conect.plus.repository.VagaCompetenciaRepository;
import com.conecta.conect.plus.repository.VagaRepository;

/**
 * Testes unitários da regra de negócio do diagnóstico de compatibilidade.
 * Não dependem de banco de dados: os repositórios são simulados com Mockito.
 */
@ExtendWith(MockitoExtension.class)
class DiagnosticoServiceTest {

    private static final Long USUARIO_ID = 1L;
    private static final Long VAGA_ID = 10L;

    private static final Long JAVA = 100L;
    private static final Long SQL = 200L;
    private static final Long DOCKER = 300L;

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private VagaRepository vagaRepository;
    @Mock private UsuarioCompetenciaRepository usuarioCompetenciaRepository;
    @Mock private VagaCompetenciaRepository vagaCompetenciaRepository;
    @Mock private CompetenciaRepository competenciaRepository;
    @Mock private CursoCompetenciaRepository cursoCompetenciaRepository;
    @Mock private CursoRepository cursoRepository;

    @InjectMocks
    private DiagnosticoService service;

    @BeforeEach
    void vagaExigeJavaSqlEDocker() {
        // lenient: alguns testes não consultam a vaga padrão
        lenient().when(vagaCompetenciaRepository.findByVagaId(VAGA_ID)).thenReturn(List.of(
                new VagaCompetencia(VAGA_ID, JAVA),
                new VagaCompetencia(VAGA_ID, SQL),
                new VagaCompetencia(VAGA_ID, DOCKER)));
    }

    private void usuarioPossui(Long... competencias) {
        List<UsuarioCompetencia> lista = java.util.Arrays.stream(competencias)
                .map(id -> new UsuarioCompetencia(USUARIO_ID, id))
                .toList();
        when(usuarioCompetenciaRepository.findByUsuarioId(USUARIO_ID)).thenReturn(lista);
    }

    @Test
    @DisplayName("Separa competências atendidas e faltantes")
    void separaAtendidasEFaltantes() {
        usuarioPossui(JAVA, SQL);

        assertThat(service.encontrarCompetenciasAtendidas(USUARIO_ID, VAGA_ID))
                .containsExactlyInAnyOrder(JAVA, SQL);
        assertThat(service.encontrarCompetenciasFaltantes(USUARIO_ID, VAGA_ID))
                .containsExactly(DOCKER);
    }

    @Test
    @DisplayName("Calcula percentual com duas casas decimais")
    void calculaPercentual() {
        usuarioPossui(JAVA, SQL);

        assertThat(service.calcularPercentualCompatibilidade(USUARIO_ID, VAGA_ID))
                .isEqualByComparingTo(new BigDecimal("66.67"));
    }

    @Test
    @DisplayName("Usuário com todas as competências tem 100%")
    void compatibilidadeTotal() {
        usuarioPossui(JAVA, SQL, DOCKER);

        assertThat(service.calcularPercentualCompatibilidade(USUARIO_ID, VAGA_ID))
                .isEqualByComparingTo("100");
        assertThat(service.encontrarCompetenciasFaltantes(USUARIO_ID, VAGA_ID)).isEmpty();
    }

    @Test
    @DisplayName("Competências do usuário fora da vaga não contam")
    void ignoraCompetenciasForaDaVaga() {
        usuarioPossui(JAVA, 999L);

        assertThat(service.calcularPercentualCompatibilidade(USUARIO_ID, VAGA_ID))
                .isEqualByComparingTo("33.33");
    }

    @Test
    @DisplayName("Vaga sem competências retorna 0% sem dividir por zero")
    void vagaSemCompetencias() {
        Long vagaVazia = 20L;
        when(vagaCompetenciaRepository.findByVagaId(vagaVazia)).thenReturn(List.of());
        usuarioPossui(JAVA);

        assertThat(service.calcularPercentualCompatibilidade(USUARIO_ID, vagaVazia))
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Recomenda cursos para as competências faltantes")
    void recomendaCursos() {
        Curso cursoDocker = new Curso(5L, "Docker na prática", "Alura", "https://exemplo.com");
        when(cursoCompetenciaRepository.findByCompetenciaId(DOCKER))
                .thenReturn(List.of(new CursoCompetencia(5L, DOCKER)));
        when(cursoRepository.findAllById(Set.of(5L))).thenReturn(List.of(cursoDocker));

        assertThat(service.encontrarCursosRecomendados(Set.of(DOCKER)))
                .extracting(Curso::getNome)
                .containsExactly("Docker na prática");
    }

    @Test
    @DisplayName("Gera o diagnóstico completo")
    void geraDiagnosticoCompleto() {
        usuarioPossui(JAVA, SQL);
        when(competenciaRepository.findAllById(anyIterable())).thenAnswer(inv -> {
            Iterable<Long> ids = inv.getArgument(0);
            List<Competencia> resultado = new java.util.ArrayList<>();
            ids.forEach(id -> resultado.add(new Competencia(id, "Competência " + id)));
            return resultado;
        });
        when(cursoCompetenciaRepository.findByCompetenciaId(DOCKER))
                .thenReturn(List.of(new CursoCompetencia(5L, DOCKER)));
        when(cursoRepository.findAllById(Set.of(5L)))
                .thenReturn(List.of(new Curso(5L, "Docker na prática", "Alura", "https://exemplo.com")));

        DiagnosticoResponseDTO diagnostico = service.gerarDiagnostico(USUARIO_ID, VAGA_ID);

        assertThat(diagnostico.getUsuarioId()).isEqualTo(USUARIO_ID);
        assertThat(diagnostico.getVagaId()).isEqualTo(VAGA_ID);
        assertThat(diagnostico.getPercentualCompatibilidade()).isEqualByComparingTo("66.67");
        assertThat(diagnostico.getCompetenciasAtendidas()).hasSize(2);
        assertThat(diagnostico.getCompetenciasFaltantes()).hasSize(1);
        assertThat(diagnostico.getCursosRecomendados()).hasSize(1);
    }
}
