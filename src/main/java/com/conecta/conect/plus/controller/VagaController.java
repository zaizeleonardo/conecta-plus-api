package com.conecta.conect.plus.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.conecta.conect.plus.dto.VagaRequestDTO;
import com.conecta.conect.plus.dto.VagaUpdateDTO;
import com.conecta.conect.plus.entity.Vaga;
import com.conecta.conect.plus.repository.VagaCompetenciaRepository;
import com.conecta.conect.plus.repository.VagaRepository;
import com.conecta.conect.plus.service.VagaService;

@RestController
@RequestMapping("/vagas")
@CrossOrigin(origins = "*")
public class VagaController {

    private final VagaRepository vagaRepository;
    private final VagaCompetenciaRepository vagaCompetenciaRepository;
    private final VagaService vagaService;

    public VagaController(
            VagaRepository vagaRepository,
            VagaCompetenciaRepository vagaCompetenciaRepository,
            VagaService vagaService) {

        this.vagaRepository = vagaRepository;
        this.vagaCompetenciaRepository = vagaCompetenciaRepository;
        this.vagaService = vagaService;
    }

    @GetMapping
    public ResponseEntity<List<Vaga>> listar() {
        return ResponseEntity.ok(vagaRepository.findAll());
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<Vaga>> minhasVagas(Authentication authentication) {

        Long usuarioId = Long.parseLong(authentication.getName());

        return ResponseEntity.ok(
                vagaRepository.findByEmpresaUsuarioId(usuarioId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vaga> buscarPorId(@PathVariable Long id) {

        return vagaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/competencias")
    public ResponseEntity<?> listarCompetencias(@PathVariable Long id) {

        if (!vagaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                vagaCompetenciaRepository.findByVagaId(id)
        );
    }

    @PostMapping
    public ResponseEntity<Vaga> criar(
            @RequestBody VagaRequestDTO dto,
            Authentication authentication) {

        Vaga vaga = vagaService.criar(dto, authentication);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vaga);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody VagaUpdateDTO dto,
            Authentication authentication) {

        try {

            Vaga vaga = vagaService.atualizar(
                    id,
                    dto,
                    authentication
            );

            return ResponseEntity.ok(vaga);

        } catch (IllegalArgumentException erro) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(erro.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(
            @PathVariable Long id,
            Authentication authentication) {

        try {

            vagaService.excluir(
                    id,
                    authentication
            );

            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException erro) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(erro.getMessage());
        }
    }
}