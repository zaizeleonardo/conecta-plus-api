package com.conecta.conect.plus.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conecta.conect.plus.dto.CompetenciaResponseDTO;
import com.conecta.conect.plus.dto.VagaResponseDTO;
import com.conecta.conect.plus.entity.Competencia;
import com.conecta.conect.plus.entity.Vaga;
import com.conecta.conect.plus.entity.VagaCompetencia;
import com.conecta.conect.plus.repository.CompetenciaRepository;
import com.conecta.conect.plus.repository.VagaCompetenciaRepository;
import com.conecta.conect.plus.repository.VagaRepository;

@RestController
@RequestMapping("/vagas")
public class VagaController {

    private final VagaRepository vagaRepository;
    private final VagaCompetenciaRepository vagaCompetenciaRepository;
    private final CompetenciaRepository competenciaRepository;

    public VagaController(
            VagaRepository vagaRepository,
            VagaCompetenciaRepository vagaCompetenciaRepository,
            CompetenciaRepository competenciaRepository) {

        this.vagaRepository = vagaRepository;
        this.vagaCompetenciaRepository = vagaCompetenciaRepository;
        this.competenciaRepository = competenciaRepository;
    }

    // ============================================================
    // GET /vagas
    // Lista todas as vagas
    // ============================================================
    @GetMapping
    public List<VagaResponseDTO> listarTodas() {

        List<Vaga> vagas = vagaRepository.findAll();

        return vagas.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    // ============================================================
    // GET /vagas/{id}
    // Busca uma vaga específica
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<VagaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return vagaRepository.findById(id)
                .map(vaga ->
                        ResponseEntity.ok(
                                converterParaDTO(vaga)
                        ))
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .build());
    }

    // ============================================================
    // GET /vagas/{id}/competencias
    // Lista as competências exigidas pela vaga
    // ============================================================
    @GetMapping("/{id}/competencias")
    public ResponseEntity<List<CompetenciaResponseDTO>> listarCompetencias(
            @PathVariable Long id) {

        // Verifica se a vaga existe
        if (!vagaRepository.existsById(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        // Busca os relacionamentos da vaga
        List<VagaCompetencia> relacionamentos =
                vagaCompetenciaRepository.findByVagaId(id);

        // Guarda os IDs das competências
        List<Long> competenciaIds = new ArrayList<>();

        for (VagaCompetencia relacionamento : relacionamentos) {
            competenciaIds.add(
                    relacionamento.getCompetenciaId()
            );
        }

        // Busca as competências no banco
        List<Competencia> competencias =
                competenciaRepository.findAllById(competenciaIds);

        // Converte para DTO
        List<CompetenciaResponseDTO> resposta =
                competencias.stream()
                        .map(competencia ->
                                new CompetenciaResponseDTO(
                                        competencia.getId(),
                                        competencia.getNome()
                                ))
                        .toList();

        return ResponseEntity.ok(resposta);
    }

    // ============================================================
    // Conversão de Entity para DTO
    // ============================================================
    private VagaResponseDTO converterParaDTO(Vaga vaga) {

        return new VagaResponseDTO(
                vaga.getId(),
                vaga.getTitulo(),
                vaga.getEmpresa(),
                vaga.getDescricao(),
                vaga.getCidade(),
                vaga.getModalidade(),
                vaga.getSalario()
        );
    }
}