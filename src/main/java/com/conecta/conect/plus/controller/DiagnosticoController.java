package com.conecta.conect.plus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conecta.conect.plus.dto.DiagnosticoResponseDTO;
import com.conecta.conect.plus.service.DiagnosticoService;

@RestController
@RequestMapping("/diagnosticos")
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    public DiagnosticoController(
            DiagnosticoService diagnosticoService) {

        this.diagnosticoService = diagnosticoService;
    }

    @GetMapping("/usuarios/{usuarioId}/vagas/{vagaId}")
    public DiagnosticoResponseDTO gerarDiagnostico(
            @PathVariable Long usuarioId,
            @PathVariable Long vagaId) {

        return diagnosticoService.gerarDiagnostico(
                usuarioId,
                vagaId
        );
    }
}