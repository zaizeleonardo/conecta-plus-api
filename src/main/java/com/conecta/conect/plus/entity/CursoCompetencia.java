package com.conecta.conect.plus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "curso_competencia")
@IdClass(CursoCompetenciaId.class)
public class CursoCompetencia {

    @Id
    private Long cursoId;

    @Id
    private Long competenciaId;

    public CursoCompetencia() {
    }

    public CursoCompetencia(Long cursoId, Long competenciaId) {
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
}