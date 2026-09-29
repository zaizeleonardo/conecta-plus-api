package com.conecta.conect.plus.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.conecta.conect.plus.dto.VagaRequestDTO;
import com.conecta.conect.plus.dto.VagaUpdateDTO;
import com.conecta.conect.plus.entity.Usuario;
import com.conecta.conect.plus.entity.Vaga;
import com.conecta.conect.plus.repository.UsuarioRepository;
import com.conecta.conect.plus.repository.VagaCompetenciaRepository;
import com.conecta.conect.plus.repository.VagaRepository;

@Service
public class VagaService {

    private final VagaRepository vagaRepository;
    private final VagaCompetenciaRepository vagaCompetenciaRepository;
    private final UsuarioRepository usuarioRepository;

    public VagaService(
            VagaRepository vagaRepository,
            VagaCompetenciaRepository vagaCompetenciaRepository,
            UsuarioRepository usuarioRepository) {

        this.vagaRepository = vagaRepository;
        this.vagaCompetenciaRepository = vagaCompetenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Vaga criar(VagaRequestDTO dto, Authentication authentication) {

        Long usuarioId = Long.parseLong(authentication.getName());

        Usuario empresaUsuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada."));

        if (!"EMPRESA".equals(empresaUsuario.getPerfil())) {
            throw new IllegalArgumentException(
                    "Apenas usuários com perfil EMPRESA podem cadastrar vagas.");
        }

        Vaga vaga = new Vaga();

        vaga.setTitulo(dto.getTitulo());
        vaga.setEmpresa(empresaUsuario.getNome());
        vaga.setDescricao(dto.getDescricao());
        vaga.setCidade(dto.getCidade());
        vaga.setModalidade(dto.getModalidade());
        vaga.setSalario(dto.getSalario());

        // Vincula a vaga à empresa que está logada
        vaga.setEmpresaUsuario(empresaUsuario);

        return vagaRepository.save(vaga);
    }

    public Vaga atualizar(
            Long id,
            VagaUpdateDTO dto,
            Authentication authentication) {

        Long usuarioId = Long.parseLong(authentication.getName());

        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vaga não encontrada."));

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        boolean isEmpresa = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_EMPRESA"));

        // Empresa só pode editar a própria vaga
        if (isEmpresa && !isAdmin) {

            if (vaga.getEmpresaUsuario() == null ||
                !vaga.getEmpresaUsuario().getId().equals(usuarioId)) {

                throw new IllegalArgumentException(
                        "Você não tem permissão para editar esta vaga.");
            }
        }

        vaga.setTitulo(dto.getTitulo());
        vaga.setEmpresa(
                vaga.getEmpresaUsuario() != null
                        ? vaga.getEmpresaUsuario().getNome()
                        : dto.getEmpresa()
        );
        vaga.setDescricao(dto.getDescricao());
        vaga.setCidade(dto.getCidade());
        vaga.setModalidade(dto.getModalidade());
        vaga.setSalario(dto.getSalario());

        return vagaRepository.save(vaga);
    }

    public void excluir(Long id, Authentication authentication) {

        Long usuarioId = Long.parseLong(authentication.getName());

        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vaga não encontrada."));

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        boolean isEmpresa = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_EMPRESA"));

        // Empresa só pode excluir a própria vaga
        if (isEmpresa && !isAdmin) {

            if (vaga.getEmpresaUsuario() == null ||
                !vaga.getEmpresaUsuario().getId().equals(usuarioId)) {

                throw new IllegalArgumentException(
                        "Você não tem permissão para excluir esta vaga.");
            }
        }

        List<com.conecta.conect.plus.entity.VagaCompetencia> relacionamentos =
                vagaCompetenciaRepository.findByVagaId(id);

        vagaCompetenciaRepository.deleteAll(relacionamentos);

        vagaRepository.deleteById(id);
    }
}