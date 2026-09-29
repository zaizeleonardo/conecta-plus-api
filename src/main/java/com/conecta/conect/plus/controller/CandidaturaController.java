package com.conecta.conect.plus.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.conecta.conect.plus.dto.CandidaturaCandidatoDTO;
import com.conecta.conect.plus.entity.Candidatura;
import com.conecta.conect.plus.service.CandidaturaService;

@RestController
@RequestMapping("/candidaturas")
public class CandidaturaController {

    private final CandidaturaService candidaturaService;

    public CandidaturaController(
            CandidaturaService candidaturaService) {

        this.candidaturaService = candidaturaService;
    }

    @PostMapping
    public ResponseEntity<?> candidatar(
            @RequestParam Long usuarioId,
            @RequestParam Long vagaId) {

        try {

            Candidatura candidatura =
                    candidaturaService.candidatar(
                            usuarioId,
                            vagaId
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(candidatura);

        } catch (IllegalArgumentException erro) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(erro.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Candidatura>> listarPorUsuario(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                candidaturaService.listarPorUsuario(usuarioId)
        );
    }

    @GetMapping("/vaga/{vagaId}")
    public ResponseEntity<?> listarPorVaga(
            @PathVariable Long vagaId,
            Authentication authentication) {

        try {

            List<CandidaturaCandidatoDTO> candidatos =
                    candidaturaService.listarPorVaga(
                            vagaId,
                            authentication
                    );

            return ResponseEntity.ok(candidatos);

        } catch (IllegalArgumentException erro) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(erro.getMessage());
        }
    }
}