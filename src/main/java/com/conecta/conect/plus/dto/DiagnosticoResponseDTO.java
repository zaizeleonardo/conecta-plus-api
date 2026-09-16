package com.conecta.conect.plus.dto;

import java.math.BigDecimal;
import java.util.List;

public class DiagnosticoResponseDTO {

    private Long usuarioId;

    private Long vagaId;

    private BigDecimal percentualCompatibilidade;

    private List<CompetenciaResponseDTO> competenciasAtendidas;

    private List<CompetenciaResponseDTO> competenciasFaltantes;

    private List<CursoResponseDTO> cursosRecomendados;

    public DiagnosticoResponseDTO() {
    }

    public DiagnosticoResponseDTO(
            Long usuarioId,
            Long vagaId,
            BigDecimal percentualCompatibilidade,
            List<CompetenciaResponseDTO> competenciasAtendidas,
            List<CompetenciaResponseDTO> competenciasFaltantes,
            List<CursoResponseDTO> cursosRecomendados) {

        this.usuarioId = usuarioId;
        this.vagaId = vagaId;
        this.percentualCompatibilidade = percentualCompatibilidade;
        this.competenciasAtendidas = competenciasAtendidas;
        this.competenciasFaltantes = competenciasFaltantes;
        this.cursosRecomendados = cursosRecomendados;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getVagaId() {
        return vagaId;
    }

    public void setVagaId(Long vagaId) {
        this.vagaId = vagaId;
    }

    public BigDecimal getPercentualCompatibilidade() {
        return percentualCompatibilidade;
    }

    public void setPercentualCompatibilidade(
            BigDecimal percentualCompatibilidade) {

        this.percentualCompatibilidade =
                percentualCompatibilidade;
    }

    public List<CompetenciaResponseDTO> getCompetenciasAtendidas() {
        return competenciasAtendidas;
    }

    public void setCompetenciasAtendidas(
            List<CompetenciaResponseDTO> competenciasAtendidas) {

        this.competenciasAtendidas =
                competenciasAtendidas;
    }

    public List<CompetenciaResponseDTO> getCompetenciasFaltantes() {
        return competenciasFaltantes;
    }

    public void setCompetenciasFaltantes(
            List<CompetenciaResponseDTO> competenciasFaltantes) {

        this.competenciasFaltantes =
                competenciasFaltantes;
    }

    public List<CursoResponseDTO> getCursosRecomendados() {
        return cursosRecomendados;
    }

    public void setCursosRecomendados(
            List<CursoResponseDTO> cursosRecomendados) {

        this.cursosRecomendados =
                cursosRecomendados;
    }
}