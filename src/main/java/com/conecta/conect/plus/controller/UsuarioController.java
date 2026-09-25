package com.conecta.conect.plus.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conecta.conect.plus.dto.CompetenciaResponseDTO;
import com.conecta.conect.plus.dto.LoginRequestDTO;
import com.conecta.conect.plus.dto.UsuarioRequestDTO;
import com.conecta.conect.plus.dto.UsuarioResponseDTO;
import com.conecta.conect.plus.dto.UsuarioUpdateDTO;
import com.conecta.conect.plus.entity.Competencia;
import com.conecta.conect.plus.entity.Usuario;
import com.conecta.conect.plus.entity.UsuarioCompetencia;
import com.conecta.conect.plus.entity.UsuarioCompetenciaId;
import com.conecta.conect.plus.repository.CompetenciaRepository;
import com.conecta.conect.plus.repository.UsuarioCompetenciaRepository;
import com.conecta.conect.plus.repository.UsuarioRepository;
import com.conecta.conect.plus.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final UsuarioCompetenciaRepository usuarioCompetenciaRepository;
    private final CompetenciaRepository competenciaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(
            UsuarioRepository usuarioRepository,
            UsuarioService usuarioService,
            UsuarioCompetenciaRepository usuarioCompetenciaRepository,
            CompetenciaRepository competenciaRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.usuarioCompetenciaRepository = usuarioCompetenciaRepository;
        this.competenciaRepository = competenciaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================================================
    // GET /usuarios
    // Lista todos os usuários
    // ============================================================

    @GetMapping
    public List<UsuarioResponseDTO> listarTodos() {

        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    // ============================================================
    // GET /usuarios/{id}
    // Busca usuário pelo ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return usuarioRepository.findById(id)
                .map(usuario ->
                        ResponseEntity.ok(
                                converterParaDTO(usuario)
                        ))
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .build());
    }

    // ============================================================
    // GET /usuarios/{id}/competencias
    // Lista competências do usuário
    // ============================================================

    @GetMapping("/{id}/competencias")
    public ResponseEntity<List<CompetenciaResponseDTO>> listarCompetencias(
            @PathVariable Long id) {

        if (!usuarioRepository.existsById(id)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        List<UsuarioCompetencia> relacionamentos =
                usuarioCompetenciaRepository.findByUsuarioId(id);

        List<Long> competenciaIds = new ArrayList<>();

        for (UsuarioCompetencia relacionamento : relacionamentos) {

            competenciaIds.add(
                    relacionamento.getCompetenciaId()
            );
        }

        List<Competencia> competencias =
                competenciaRepository.findAllById(competenciaIds);

        List<CompetenciaResponseDTO> resposta =
                competencias.stream()
                        .map(competencia ->
                                new CompetenciaResponseDTO(
                                        competencia.getId(),
                                        competencia.getNome()
                                ))
                        .toList();

        return ResponseEntity.ok(resposta);
    }

    // ============================================================
    // POST /usuarios/{usuarioId}/competencias/{competenciaId}
    // Adiciona competência ao usuário
    // ============================================================

    @PostMapping("/{usuarioId}/competencias/{competenciaId}")
    public ResponseEntity<CompetenciaResponseDTO> adicionarCompetencia(
            @PathVariable Long usuarioId,
            @PathVariable Long competenciaId) {

        if (!usuarioRepository.existsById(usuarioId)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        Competencia competencia =
                competenciaRepository.findById(competenciaId)
                        .orElse(null);

        if (competencia == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        UsuarioCompetenciaId id =
                new UsuarioCompetenciaId(
                        usuarioId,
                        competenciaId
                );

        if (usuarioCompetenciaRepository.existsById(id)) {

            throw new IllegalArgumentException(
                    "O usuário já possui essa competência."
            );
        }

        UsuarioCompetencia relacionamento =
                new UsuarioCompetencia(
                        usuarioId,
                        competenciaId
                );

        usuarioCompetenciaRepository.save(relacionamento);

        CompetenciaResponseDTO resposta =
                new CompetenciaResponseDTO(
                        competencia.getId(),
                        competencia.getNome()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resposta);
    }

    // ============================================================
    // DELETE /usuarios/{usuarioId}/competencias/{competenciaId}
    // Remove competência do usuário
    // ============================================================

    @DeleteMapping("/{usuarioId}/competencias/{competenciaId}")
    public ResponseEntity<Void> removerCompetencia(
            @PathVariable Long usuarioId,
            @PathVariable Long competenciaId) {

        if (!usuarioRepository.existsById(usuarioId)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        if (!competenciaRepository.existsById(competenciaId)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        UsuarioCompetenciaId id =
                new UsuarioCompetenciaId(
                        usuarioId,
                        competenciaId
                );

        if (!usuarioCompetenciaRepository.existsById(id)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        usuarioCompetenciaRepository.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // ============================================================
    // POST /usuarios
    // Cria novo usuário
    // ============================================================

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(
            @Valid @RequestBody UsuarioRequestDTO request) {

        Usuario usuario = new Usuario();

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());
        usuario.setTelefone(request.getTelefone());
        usuario.setCidade(request.getCidade());

        usuario.setObjetivoProfissional(
                request.getObjetivoProfissional()
        );

        Usuario usuarioSalvo =
                usuarioService.salvar(usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(converterParaDTO(usuarioSalvo));
    }

    // ============================================================
    // POST /usuarios/login
    // Realiza login
    // ============================================================

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(
            @RequestBody LoginRequestDTO request) {

        Usuario usuario =
                usuarioRepository
                        .findByEmail(request.getEmail())
                        .orElse(null);

        if (usuario == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        boolean senhaCorreta =
                passwordEncoder.matches(
                        request.getSenha(),
                        usuario.getSenha()
                );

        if (!senhaCorreta) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        return ResponseEntity.ok(
                converterParaDTO(usuario)
        );
    }

    // ============================================================
    // PUT /usuarios/{id}
    // Atualiza usuário
    // ============================================================

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO request) {

        return usuarioRepository.findById(id)
                .map(usuario -> {

                    if (!usuario.getEmail().equals(request.getEmail())
                            && usuarioRepository
                                    .existsByEmail(request.getEmail())) {

                        throw new IllegalArgumentException(
                                "E-mail já cadastrado."
                        );
                    }

                    usuario.setNome(request.getNome());
                    usuario.setEmail(request.getEmail());
                    usuario.setTelefone(request.getTelefone());
                    usuario.setCidade(request.getCidade());

                    usuario.setObjetivoProfissional(
                            request.getObjetivoProfissional()
                    );

                    Usuario usuarioAtualizado =
                            usuarioRepository.save(usuario);

                    return ResponseEntity.ok(
                            converterParaDTO(usuarioAtualizado)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .build());
    }

    // ============================================================
    // DELETE /usuarios/{id}
    // Exclui usuário
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuario(
            @PathVariable Long id) {

        if (!usuarioRepository.existsById(id)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        usuarioRepository.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

 // ============================================================
    // Converte Entity para DTO
    // ============================================================

    private UsuarioResponseDTO converterParaDTO(
            Usuario usuario) {

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getCidade(),
                usuario.getObjetivoProfissional(),
                usuario.getPerfil()
        );
    }
}