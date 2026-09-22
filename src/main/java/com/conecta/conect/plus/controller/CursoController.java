package com.conecta.conect.plus.controller;

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

import com.conecta.conect.plus.dto.CursoRequestDTO;
import com.conecta.conect.plus.dto.CursoResponseDTO;
import com.conecta.conect.plus.dto.CursoUpdateDTO;
import com.conecta.conect.plus.entity.Curso;
import com.conecta.conect.plus.repository.CursoRepository;
import com.conecta.conect.plus.service.CursoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoRepository cursoRepository;
    private final CursoService cursoService;

    public CursoController(
            CursoRepository cursoRepository,
            CursoService cursoService) {

        this.cursoRepository = cursoRepository;
        this.cursoService = cursoService;
    }

    // =========================
    // LISTAR TODOS OS CURSOS
    // =========================

    @GetMapping
    public List<CursoResponseDTO> listarTodos() {

        List<Curso> cursos = cursoRepository.findAll();

        return cursos.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    // =========================
    // BUSCAR CURSO POR ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return cursoRepository.findById(id)
                .map(curso ->
                        ResponseEntity.ok(
                                converterParaDTO(curso)
                        ))
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .build());
    }

    // =========================
    // CRIAR CURSO
    // =========================

    @PostMapping
    public ResponseEntity<CursoResponseDTO> criar(
            @Valid @RequestBody CursoRequestDTO dto) {

        Curso curso = cursoService.criar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(converterParaDTO(curso));
    }

    // =========================
    // ATUALIZAR CURSO
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CursoUpdateDTO dto) {

        try {

            Curso curso =
                    cursoService.atualizar(id, dto);

            return ResponseEntity.ok(
                    converterParaDTO(curso)
            );

        } catch (IllegalArgumentException erro) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    // =========================
    // EXCLUIR CURSO
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        try {

            cursoService.excluir(id);

            return ResponseEntity
                    .noContent()
                    .build();

        } catch (IllegalArgumentException erro) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    // =========================
    // CONVERTER ENTITY → DTO
    // =========================

    private CursoResponseDTO converterParaDTO(
            Curso curso) {

        return new CursoResponseDTO(
                curso.getId(),
                curso.getNome(),
                curso.getPlataforma(),
                curso.getUrl()
        );
    }
}