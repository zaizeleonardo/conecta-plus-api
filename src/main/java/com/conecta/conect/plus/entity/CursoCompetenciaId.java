package com.conecta.conect.plus.entity;

import java.io.Serializable;
import java.util.Objects;

public class CursoCompetenciaId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cursoId;
    private Long competenciaId;

    public CursoCompetenciaId() {
    }

    public CursoCompetenciaId(Long cursoId, Long competenciaId) {
        this.cursoId = cursoId;
        this.competenciaId = competenciaId;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
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

        if (!(o instanceof CursoCompetenciaId)) {
            return false;
        }

        CursoCompetenciaId that = (CursoCompetenciaId) o;

        return Objects.equals(cursoId, that.cursoId)
                && Objects.equals(competenciaId, that.competenciaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cursoId, competenciaId);
    }
}