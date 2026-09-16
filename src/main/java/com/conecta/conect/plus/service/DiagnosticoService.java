package com.conecta.conect.plus.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.conecta.conect.plus.dto.CompetenciaResponseDTO;
import com.conecta.conect.plus.dto.CursoResponseDTO;
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

@Service
public class DiagnosticoService {

    private final UsuarioRepository usuarioRepository;
    private final VagaRepository vagaRepository;
    private final UsuarioCompetenciaRepository usuarioCompetenciaRepository;
    private final VagaCompetenciaRepository vagaCompetenciaRepository;
    private final CompetenciaRepository competenciaRepository;
    private final CursoCompetenciaRepository cursoCompetenciaRepository;
    private final CursoRepository cursoRepository;

    public DiagnosticoService(
            UsuarioRepository usuarioRepository,
            VagaRepository vagaRepository,
            UsuarioCompetenciaRepository usuarioCompetenciaRepository,
            VagaCompetenciaRepository vagaCompetenciaRepository,
            CompetenciaRepository competenciaRepository,
            CursoCompetenciaRepository cursoCompetenciaRepository,
            CursoRepository cursoRepository) {

        this.usuarioRepository = usuarioRepository;
        this.vagaRepository = vagaRepository;
        this.usuarioCompetenciaRepository = usuarioCompetenciaRepository;
        this.vagaCompetenciaRepository = vagaCompetenciaRepository;
        this.competenciaRepository = competenciaRepository;
        this.cursoCompetenciaRepository = cursoCompetenciaRepository;
        this.cursoRepository = cursoRepository;
    }

    // ============================================================
    // BUSCAR COMPETÊNCIAS DO USUÁRIO
    // ============================================================

    public Set<Long> buscarCompetenciasDoUsuario(Long usuarioId) {

        List<UsuarioCompetencia> relacionamentos =
                usuarioCompetenciaRepository.findByUsuarioId(usuarioId);

        Set<Long> competenciaIds = new HashSet<>();

        for (UsuarioCompetencia relacionamento : relacionamentos) {

            competenciaIds.add(
                    relacionamento.getCompetenciaId()
            );
        }

        return competenciaIds;
    }

    // ============================================================
    // BUSCAR COMPETÊNCIAS DA VAGA
    // ============================================================

    public Set<Long> buscarCompetenciasDaVaga(Long vagaId) {

        List<VagaCompetencia> relacionamentos =
                vagaCompetenciaRepository.findByVagaId(vagaId);

        Set<Long> competenciaIds = new HashSet<>();

        for (VagaCompetencia relacionamento : relacionamentos) {

            competenciaIds.add(
                    relacionamento.getCompetenciaId()
            );
        }

        return competenciaIds;
    }

    // ============================================================
    // ENCONTRAR COMPETÊNCIAS ATENDIDAS
    // ============================================================

    public Set<Long> encontrarCompetenciasAtendidas(
            Long usuarioId,
            Long vagaId) {

        Set<Long> competenciasUsuario =
                buscarCompetenciasDoUsuario(usuarioId);

        Set<Long> competenciasVaga =
                buscarCompetenciasDaVaga(vagaId);

        Set<Long> atendidas = new HashSet<>();

        for (Long competenciaId : competenciasVaga) {

            if (competenciasUsuario.contains(competenciaId)) {

                atendidas.add(competenciaId);
            }
        }

        return atendidas;
    }

    // ============================================================
    // ENCONTRAR COMPETÊNCIAS FALTANTES
    // ============================================================

    public Set<Long> encontrarCompetenciasFaltantes(
            Long usuarioId,
            Long vagaId) {

        Set<Long> competenciasUsuario =
                buscarCompetenciasDoUsuario(usuarioId);

        Set<Long> competenciasVaga =
                buscarCompetenciasDaVaga(vagaId);

        Set<Long> faltantes = new HashSet<>();

        for (Long competenciaId : competenciasVaga) {

            if (!competenciasUsuario.contains(competenciaId)) {

                faltantes.add(competenciaId);
            }
        }

        return faltantes;
    }

    // ============================================================
    // CALCULAR PERCENTUAL DE COMPATIBILIDADE
    // ============================================================

    public BigDecimal calcularPercentualCompatibilidade(
            Long usuarioId,
            Long vagaId) {

        Set<Long> competenciasAtendidas =
                encontrarCompetenciasAtendidas(
                        usuarioId,
                        vagaId
                );

        Set<Long> competenciasVaga =
                buscarCompetenciasDaVaga(vagaId);

        int quantidadeAtendidas =
                competenciasAtendidas.size();

        int quantidadeExigidas =
                competenciasVaga.size();

        if (quantidadeExigidas == 0) {

            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(quantidadeAtendidas)
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        BigDecimal.valueOf(quantidadeExigidas),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    // ============================================================
    // CONVERTER COMPETÊNCIAS PARA DTO
    // ============================================================

    private List<CompetenciaResponseDTO> converterCompetencias(
            Set<Long> competenciaIds) {

        List<Competencia> competencias =
                competenciaRepository.findAllById(
                        competenciaIds
                );

        return competencias.stream()
                .map(competencia ->
                        new CompetenciaResponseDTO(
                                competencia.getId(),
                                competencia.getNome()
                        )
                )
                .toList();
    }

    // ============================================================
    // ENCONTRAR CURSOS RECOMENDADOS
    // ============================================================

    public List<Curso> encontrarCursosRecomendados(
            Set<Long> competenciasFaltantes) {

        Set<Long> cursoIds = new HashSet<>();

        for (Long competenciaId : competenciasFaltantes) {

            List<CursoCompetencia> relacionamentos =
                    cursoCompetenciaRepository
                            .findByCompetenciaId(competenciaId);

            for (CursoCompetencia relacionamento : relacionamentos) {

                cursoIds.add(
                        relacionamento.getCursoId()
                );
            }
        }

        return cursoRepository.findAllById(cursoIds);
    }

    // ============================================================
    // CONVERTER CURSOS PARA DTO
    // ============================================================

    private List<CursoResponseDTO> converterCursos(
            List<Curso> cursos) {

        return cursos.stream()
                .map(curso ->
                        new CursoResponseDTO(
                                curso.getId(),
                                curso.getNome(),
                                curso.getPlataforma(),
                                curso.getUrl()
                        )
                )
                .toList();
    }

    // ============================================================
    // GERAR DIAGNÓSTICO COMPLETO
    // ============================================================

    public DiagnosticoResponseDTO gerarDiagnostico(
            Long usuarioId,
            Long vagaId) {

        // 1. Encontrar competências que o usuário possui
        Set<Long> competenciasAtendidas =
                encontrarCompetenciasAtendidas(
                        usuarioId,
                        vagaId
                );

        // 2. Encontrar competências que estão faltando
        Set<Long> competenciasFaltantes =
                encontrarCompetenciasFaltantes(
                        usuarioId,
                        vagaId
                );

        // 3. Calcular percentual de compatibilidade
        BigDecimal percentual =
                calcularPercentualCompatibilidade(
                        usuarioId,
                        vagaId
                );

        // 4. Buscar cursos relacionados às competências faltantes
        List<Curso> cursos =
                encontrarCursosRecomendados(
                        competenciasFaltantes
                );

        // 5. Converter competências para DTO
        List<CompetenciaResponseDTO> atendidas =
                converterCompetencias(
                        competenciasAtendidas
                );

        List<CompetenciaResponseDTO> faltantes =
                converterCompetencias(
                        competenciasFaltantes
                );

        // 6. Converter cursos para DTO
        List<CursoResponseDTO> cursosRecomendados =
                converterCursos(cursos);

        // 7. Montar a resposta final
        return new DiagnosticoResponseDTO(
                usuarioId,
                vagaId,
                percentual,
                atendidas,
                faltantes,
                cursosRecomendados
        );
    }
}