package com.conecta.conect.plus.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conecta.conect.plus.dto.CompetenciaResponseDTO;
import com.conecta.conect.plus.entity.Competencia;
import com.conecta.conect.plus.repository.CompetenciaRepository;

@RestController
@RequestMapping("/competencias")
public class CompetenciaController {

    private final CompetenciaRepository competenciaRepository;

    public CompetenciaController(
            CompetenciaRepository competenciaRepository) {

        this.competenciaRepository = competenciaRepository;
    }

    // GET /competencias
    @GetMapping
    public List<CompetenciaResponseDTO> listarTodas() {

        List<Competencia> competencias =
                competenciaRepository.findAll();

        return competencias.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    // Conversão de Entity para DTO
    private CompetenciaResponseDTO converterParaDTO(
            Competencia competencia) {

        return new CompetenciaResponseDTO(
                competencia.getId(),
                competencia.getNome()
        );
    }
}