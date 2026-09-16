package com.conecta.conect.plus.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conecta.conect.plus.entity.Candidatura;
import com.conecta.conect.plus.repository.CandidaturaRepository;

@Service
public class CandidaturaService {

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    public Candidatura candidatar(Long usuarioId, Long vagaId) {

        if (candidaturaRepository
                .existsByUsuarioIdAndVagaId(usuarioId, vagaId)) {

            throw new IllegalArgumentException(
                    "O candidato já está inscrito nesta vaga."
            );
        }

        Candidatura candidatura = new Candidatura();

        candidatura.setUsuarioId(usuarioId);
        candidatura.setVagaId(vagaId);
        candidatura.setDataCandidatura(LocalDateTime.now());
        candidatura.setStatus("ENVIADA");

        return candidaturaRepository.save(candidatura);
    }

    public List<Candidatura> listarPorUsuario(Long usuarioId) {

        return candidaturaRepository
                .findByUsuarioId(usuarioId);
    }

    public List<Candidatura> listarPorVaga(Long vagaId) {

        return candidaturaRepository
                .findByVagaId(vagaId);
    }
}