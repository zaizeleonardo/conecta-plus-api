package com.conecta.conect.plus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conecta.conect.plus.entity.Vaga;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    List<Vaga> findByEmpresaUsuarioId(Long empresaId);

    Optional<Vaga> findByIdAndEmpresaUsuarioId(Long id, Long empresaId);
}