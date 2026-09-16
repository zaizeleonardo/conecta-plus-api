package com.conecta.conect.plus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conecta.conect.plus.entity.VagaCompetencia;
import com.conecta.conect.plus.entity.VagaCompetenciaId;

public interface VagaCompetenciaRepository
        extends JpaRepository<VagaCompetencia, VagaCompetenciaId> {

    List<VagaCompetencia> findByVagaId(Long vagaId);

    List<VagaCompetencia> findByCompetenciaId(Long competenciaId);
}