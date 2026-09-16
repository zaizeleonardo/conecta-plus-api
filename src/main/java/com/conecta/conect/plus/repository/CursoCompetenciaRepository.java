package com.conecta.conect.plus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conecta.conect.plus.entity.CursoCompetencia;
import com.conecta.conect.plus.entity.CursoCompetenciaId;

public interface CursoCompetenciaRepository
        extends JpaRepository<CursoCompetencia, CursoCompetenciaId> {

    List<CursoCompetencia> findByCompetenciaId(Long competenciaId);

    List<CursoCompetencia> findByCursoId(Long cursoId);
}