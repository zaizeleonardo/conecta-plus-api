package com.conecta.conect.plus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conecta.conect.plus.entity.UsuarioCompetencia;
import com.conecta.conect.plus.entity.UsuarioCompetenciaId;

public interface UsuarioCompetenciaRepository
        extends JpaRepository<UsuarioCompetencia, UsuarioCompetenciaId> {

    List<UsuarioCompetencia> findByUsuarioId(Long usuarioId);

    List<UsuarioCompetencia> findByCompetenciaId(Long competenciaId);
}