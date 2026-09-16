package com.conecta.conect.plus.entity;

import java.io.Serializable;
import java.util.Objects;

public class UsuarioCompetenciaId implements Serializable {

    private Long usuarioId;
    private Long competenciaId;

    public UsuarioCompetenciaId() {
    }

    public UsuarioCompetenciaId(Long usuarioId, Long competenciaId) {
        this.usuarioId = usuarioId;
        this.competenciaId = competenciaId;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof UsuarioCompetenciaId)) {
            return false;
        }

        UsuarioCompetenciaId that = (UsuarioCompetenciaId) o;

        return Objects.equals(usuarioId, that.usuarioId)
                && Objects.equals(competenciaId, that.competenciaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, competenciaId);
    }
}