package com.conecta.conect.plus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conecta.conect.plus.entity.Candidatura;

public interface CandidaturaRepository
        extends JpaRepository<Candidatura, Long> {

    List<Candidatura> findByUsuarioId(Long usuarioId);

    List<Candidatura> findByVagaId(Long vagaId);

    Optional<Candidatura> findByUsuarioIdAndVagaId(
            Long usuarioId,
            Long vagaId
    );

    boolean existsByUsuarioIdAndVagaId(
            Long usuarioId,
            Long vagaId
    );
}