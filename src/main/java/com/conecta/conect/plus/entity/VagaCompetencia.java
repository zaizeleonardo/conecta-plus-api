package com.conecta.conect.plus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "vaga_competencia")
@IdClass(VagaCompetenciaId.class)
public class VagaCompetencia {

    @Id
    private Long vagaId;

    @Id
    private Long competenciaId;

    public VagaCompetencia() {
    }

    public VagaCompetencia(
            Long vagaId,
            Long competenciaId) {

        this.vagaId = vagaId;
        this.competenciaId = competenciaId;
    }

    public Long getVagaId() {
        return vagaId;
    }

    public void setVagaId(Long vagaId) {
        this.vagaId = vagaId;
    }

    public Long getCompetenciaId() {
        return competenciaId;
    }

    public void setCompetenciaId(Long competenciaId) {
        this.competenciaId = competenciaId;
    }
}