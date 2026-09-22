package com.conecta.conect.plus.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.conecta.conect.plus.dto.VagaRequestDTO;
import com.conecta.conect.plus.dto.VagaUpdateDTO;
import com.conecta.conect.plus.entity.Vaga;
import com.conecta.conect.plus.repository.VagaCompetenciaRepository;
import com.conecta.conect.plus.repository.VagaRepository;

@Service
public class VagaService {

    private final VagaRepository vagaRepository;
    private final VagaCompetenciaRepository vagaCompetenciaRepository;

    public VagaService(
            VagaRepository vagaRepository,
            VagaCompetenciaRepository vagaCompetenciaRepository) {

        this.vagaRepository = vagaRepository;
        this.vagaCompetenciaRepository = vagaCompetenciaRepository;
    }

    public Vaga criar(VagaRequestDTO dto) {

        Vaga vaga = new Vaga();

        vaga.setTitulo(dto.getTitulo());
        vaga.setEmpresa(dto.getEmpresa());
        vaga.setDescricao(dto.getDescricao());
        vaga.setCidade(dto.getCidade());
        vaga.setModalidade(dto.getModalidade());
        vaga.setSalario(dto.getSalario());

        return vagaRepository.save(vaga);
    }

    public Vaga atualizar(Long id, VagaUpdateDTO dto) {

        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Vaga não encontrada."
                        )
                );

        vaga.setTitulo(dto.getTitulo());
        vaga.setEmpresa(dto.getEmpresa());
        vaga.setDescricao(dto.getDescricao());
        vaga.setCidade(dto.getCidade());
        vaga.setModalidade(dto.getModalidade());
        vaga.setSalario(dto.getSalario());

        return vagaRepository.save(vaga);
    }

    public void excluir(Long id) {

        if (!vagaRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Vaga não encontrada."
            );
        }

        List<com.conecta.conect.plus.entity.VagaCompetencia> relacionamentos =
                vagaCompetenciaRepository.findByVagaId(id);

        vagaCompetenciaRepository.deleteAll(relacionamentos);

        vagaRepository.deleteById(id);
    }
}