package com.conecta.conect.plus.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.conecta.conect.plus.dto.CandidaturaCandidatoDTO;
import com.conecta.conect.plus.entity.Candidatura;
import com.conecta.conect.plus.entity.Usuario;
import com.conecta.conect.plus.entity.Vaga;
import com.conecta.conect.plus.repository.CandidaturaRepository;
import com.conecta.conect.plus.repository.UsuarioRepository;
import com.conecta.conect.plus.repository.VagaRepository;

@Service
public class CandidaturaService {

    private final CandidaturaRepository candidaturaRepository;
    private final VagaRepository vagaRepository;
    private final UsuarioRepository usuarioRepository;

    public CandidaturaService(
            CandidaturaRepository candidaturaRepository,
            VagaRepository vagaRepository,
            UsuarioRepository usuarioRepository) {

        this.candidaturaRepository = candidaturaRepository;
        this.vagaRepository = vagaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ============================================================
    // CANDIDATAR
    // ============================================================

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

    // ============================================================
    // LISTAR CANDIDATURAS DO USUÁRIO
    // ============================================================

    public List<Candidatura> listarPorUsuario(Long usuarioId) {

        return candidaturaRepository
                .findByUsuarioId(usuarioId);
    }

    // ============================================================
    // LISTAR CANDIDATOS DE UMA VAGA
    // ============================================================

    public List<CandidaturaCandidatoDTO> listarPorVaga(
            Long vagaId,
            Authentication authentication) {

        // --------------------------------------------------------
        // Verifica se a vaga existe
        // --------------------------------------------------------

        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Vaga não encontrada."
                        )
                );

        // --------------------------------------------------------
        // ID do usuário logado
        // --------------------------------------------------------

        Long usuarioLogadoId =
                Long.parseLong(authentication.getName());

        // --------------------------------------------------------
        // Verifica perfil
        // --------------------------------------------------------

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_ADMIN")
                );

        boolean isEmpresa = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_EMPRESA")
                );

        // --------------------------------------------------------
        // Empresa só pode visualizar candidatos
        // das próprias vagas
        // --------------------------------------------------------

        if (isEmpresa && !isAdmin) {

            if (vaga.getEmpresaUsuario() == null ||
                !vaga.getEmpresaUsuario()
                        .getId()
                        .equals(usuarioLogadoId)) {

                throw new IllegalArgumentException(
                        "Você não tem permissão para visualizar os candidatos desta vaga."
                );
            }
        }

        // --------------------------------------------------------
        // Busca candidaturas
        // --------------------------------------------------------

        List<Candidatura> candidaturas =
                candidaturaRepository.findByVagaId(vagaId);

        // --------------------------------------------------------
        // Converte para DTO com dados do candidato
        // --------------------------------------------------------

        return candidaturas.stream()
                .map(candidatura -> {

                    Usuario usuario =
                            usuarioRepository
                                    .findById(
                                            candidatura.getUsuarioId()
                                    )
                                    .orElse(null);

                    if (usuario == null) {
                        return null;
                    }

                    return new CandidaturaCandidatoDTO(
                            candidatura.getId(),
                            candidatura.getUsuarioId(),
                            candidatura.getVagaId(),
                            usuario.getNome(),
                            usuario.getEmail(),
                            usuario.getTelefone(),
                            usuario.getCidade(),
                            candidatura.getDataCandidatura(),
                            candidatura.getStatus()
                    );
                })
                .filter(dto -> dto != null)
                .toList();
    }
}