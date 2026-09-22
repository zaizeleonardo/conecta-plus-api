package com.conecta.conect.plus.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.conecta.conect.plus.dto.CursoRequestDTO;
import com.conecta.conect.plus.dto.CursoUpdateDTO;
import com.conecta.conect.plus.entity.Curso;
import com.conecta.conect.plus.entity.CursoCompetencia;
import com.conecta.conect.plus.repository.CursoCompetenciaRepository;
import com.conecta.conect.plus.repository.CursoRepository;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final CursoCompetenciaRepository cursoCompetenciaRepository;

    public CursoService(
            CursoRepository cursoRepository,
            CursoCompetenciaRepository cursoCompetenciaRepository) {

        this.cursoRepository = cursoRepository;
        this.cursoCompetenciaRepository = cursoCompetenciaRepository;
    }

    // =========================
    // CRIAR CURSO
    // =========================

    public Curso criar(CursoRequestDTO dto) {

        Curso curso = new Curso();

        curso.setNome(dto.getNome());
        curso.setPlataforma(dto.getPlataforma());
        curso.setUrl(dto.getUrl());

        return cursoRepository.save(curso);
    }

    // =========================
    // ATUALIZAR CURSO
    // =========================

    public Curso atualizar(
            Long id,
            CursoUpdateDTO dto) {

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Curso não encontrado."
                        )
                );

        curso.setNome(dto.getNome());
        curso.setPlataforma(dto.getPlataforma());
        curso.setUrl(dto.getUrl());

        return cursoRepository.save(curso);
    }

    // =========================
    // EXCLUIR CURSO
    // =========================

    public void excluir(Long id) {

        if (!cursoRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Curso não encontrado."
            );
        }

        List<CursoCompetencia> relacionamentos =
                cursoCompetenciaRepository.findByCursoId(id);

        cursoCompetenciaRepository.deleteAll(relacionamentos);

        cursoRepository.deleteById(id);
    }
}