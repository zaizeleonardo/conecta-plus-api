package com.conecta.conect.plus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.conecta.conect.plus.entity.Candidatura;
import com.conecta.conect.plus.service.CandidaturaService;

@RestController
@RequestMapping("/candidaturas")
public class CandidaturaController {

    @Autowired
    private CandidaturaService candidaturaService;

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
    public ResponseEntity<List<Candidatura>> listarPorVaga(
            @PathVariable Long vagaId) {

        return ResponseEntity.ok(
                candidaturaService.listarPorVaga(vagaId)
        );
    }
}