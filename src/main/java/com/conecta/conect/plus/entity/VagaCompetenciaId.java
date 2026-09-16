package com.conecta.conect.plus.entity;

import java.io.Serializable;
import java.util.Objects;

public class VagaCompetenciaId implements Serializable {

    private Long vagaId;

    private Long competenciaId;

    public VagaCompetenciaId() {
    }

    public VagaCompetenciaId(
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

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof VagaCompetenciaId)) {
            return false;
        }

        VagaCompetenciaId that =
                (VagaCompetenciaId) o;

        return Objects.equals(vagaId, that.vagaId)
                && Objects.equals(
                        competenciaId,
                        that.competenciaId
                );
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                vagaId,
                competenciaId
        );
    }
}