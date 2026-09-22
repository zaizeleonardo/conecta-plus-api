package com.conecta.conect.plus.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conecta.conect.plus.dto.CompetenciaResponseDTO;
import com.conecta.conect.plus.dto.VagaRequestDTO;
import com.conecta.conect.plus.dto.VagaResponseDTO;
import com.conecta.conect.plus.dto.VagaUpdateDTO;
import com.conecta.conect.plus.entity.Competencia;
import com.conecta.conect.plus.entity.Vaga;
import com.conecta.conect.plus.entity.VagaCompetencia;
import com.conecta.conect.plus.repository.CompetenciaRepository;
import com.conecta.conect.plus.repository.VagaCompetenciaRepository;
import com.conecta.conect.plus.repository.VagaRepository;
import com.conecta.conect.plus.service.VagaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vagas")
public class VagaController {

    private final VagaRepository vagaRepository;
    private final VagaCompetenciaRepository vagaCompetenciaRepository;
    private final CompetenciaRepository competenciaRepository;
    private final VagaService vagaService;

    public VagaController(
            VagaRepository vagaRepository,
            VagaCompetenciaRepository vagaCompetenciaRepository,
            CompetenciaRepository competenciaRepository,
            VagaService vagaService) {

        this.vagaRepository = vagaRepository;
        this.vagaCompetenciaRepository = vagaCompetenciaRepository;
        this.competenciaRepository = competenciaRepository;
        this.vagaService = vagaService;
    }

    // =========================
    // LISTAR TODAS AS VAGAS
    // =========================

    @GetMapping
    public List<VagaResponseDTO> listarTodas() {

        List<Vaga> vagas = vagaRepository.findAll();

        return vagas.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    // =========================
    // BUSCAR VAGA POR ID
    // =========================

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

    // =========================
    // LISTAR COMPETÊNCIAS DA VAGA
    // =========================

    @GetMapping("/{id}/competencias")
    public ResponseEntity<List<CompetenciaResponseDTO>> listarCompetencias(
            @PathVariable Long id) {

        if (!vagaRepository.existsById(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        List<VagaCompetencia> relacionamentos =
                vagaCompetenciaRepository.findByVagaId(id);

        List<Long> competenciaIds = new ArrayList<>();

        for (VagaCompetencia relacionamento : relacionamentos) {

            competenciaIds.add(
                    relacionamento.getCompetenciaId()
            );
        }

        List<Competencia> competencias =
                competenciaRepository.findAllById(competenciaIds);

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

    // =========================
    // CRIAR VAGA
    // =========================

    @PostMapping
    public ResponseEntity<VagaResponseDTO> criar(
            @Valid @RequestBody VagaRequestDTO dto) {

        Vaga vaga = vagaService.criar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(converterParaDTO(vaga));
    }

    // =========================
    // ATUALIZAR VAGA
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<VagaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VagaUpdateDTO dto) {

        try {

            Vaga vaga = vagaService.atualizar(id, dto);

            return ResponseEntity.ok(
                    converterParaDTO(vaga)
            );

        } catch (IllegalArgumentException erro) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    // =========================
    // EXCLUIR VAGA
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        try {

            vagaService.excluir(id);

            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException erro) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    // =========================
    // CONVERTER ENTITY → DTO
    // =========================

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