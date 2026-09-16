package com.conecta.conect.plus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario_competencia")
@IdClass(UsuarioCompetenciaId.class)
public class UsuarioCompetencia {

    @Id
    private Long usuarioId;

    @Id
    private Long competenciaId;

    public UsuarioCompetencia() {
    }

    public UsuarioCompetencia(Long usuarioId, Long competenciaId) {
        this.usuarioId = usuarioId;
        this.competenciaId = competenciaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getCompetenciaId() {
        return competenciaId;
    }

    public void setCompetenciaId(Long competenciaId) {
        this.competenciaId = competenciaId;
    }
}