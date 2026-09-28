package com.conecta.conect.plus.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conecta.conect.plus.dto.CompetenciaResponseDTO;
import com.conecta.conect.plus.dto.VagaRequestDTO;
import com.conecta.conect.plus.dto.VagaResponseDTO;
import com.conecta.conect.plus.dto.VagaUpdateDTO;
import com.conecta.conect.plus.entity.Competencia;
import com.conecta.conect.plus.entity.Vaga;
import com.conecta.conect.plus.entity.VagaCompetencia;
import com.conecta.conect.plus.repository.CompetenciaRepository;
import com.conecta.conect.plus.repository.VagaCompetenciaRepository;
import com.conecta.conect.plus.repository.VagaRepository;
import com.conecta.conect.plus.service.VagaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vagas")
public class VagaController {

    private final VagaRepository vagaRepository;
    private final VagaCompetenciaRepository vagaCompetenciaRepository;
    private final CompetenciaRepository competenciaRepository;
    private final VagaService vagaService;
    private final DataSource dataSource;

    public VagaController(
            VagaRepository vagaRepository,
            VagaCompetenciaRepository vagaCompetenciaRepository,
            CompetenciaRepository competenciaRepository,
            VagaService vagaService,
            DataSource dataSource) {

        this.vagaRepository = vagaRepository;
        this.vagaCompetenciaRepository = vagaCompetenciaRepository;
        this.competenciaRepository = competenciaRepository;
        this.vagaService = vagaService;
        this.dataSource = dataSource;
    }

    // =========================
    // LISTAR TODAS AS VAGAS
    // =========================

    @GetMapping
    public List<VagaResponseDTO> listarTodas() {

        List<Vaga> vagas = vagaRepository.findAll();

        if (!vagas.isEmpty()) {

            System.out.println(
                    ">>> CIDADE LIDA PELO JAVA: "
                    + vagas.get(0).getCidade()
            );

            System.out.println(
                    ">>> UNICODE: "
                    + vagas.get(0).getCidade()
                            .codePoints()
                            .mapToObj(c -> String.format("\\u%04X", c))
                            .toList()
            );
        }

        // =========================
        // DIAGNÓSTICO DA CONEXÃO JDBC
        // =========================

        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT "
                        + "@@character_set_client AS client_charset, "
                        + "@@character_set_connection AS connection_charset, "
                        + "@@character_set_results AS results_charset, "
                        + "@@character_set_database AS database_charset, "
                        + "@@character_set_server AS server_charset"
                )
        ) {

            if (resultSet.next()) {

                System.out.println(
                        ">>> JDBC CHARSET CLIENT: "
                        + resultSet.getString("client_charset")
                );

                System.out.println(
                        ">>> JDBC CHARSET CONNECTION: "
                        + resultSet.getString("connection_charset")
                );

                System.out.println(
                        ">>> JDBC CHARSET RESULTS: "
                        + resultSet.getString("results_charset")
                );

                System.out.println(
                        ">>> JDBC CHARSET DATABASE: "
                        + resultSet.getString("database_charset")
                );

                System.out.println(
                        ">>> JDBC CHARSET SERVER: "
                        + resultSet.getString("server_charset")
                );
            }

        } catch (Exception erro) {

            System.out.println(
                    ">>> ERRO AO CONSULTAR CHARSET JDBC: "
                    + erro.getMessage()
            );
        }

        // =========================
        // TESTE JDBC DA CIDADE
        // =========================

        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT cidade, HEX(cidade) AS bytes_cidade "
                        + "FROM vagas WHERE id = 1"
                )
        ) {

            if (resultSet.next()) {

                String cidadeJdbc = resultSet.getString("cidade");
                String hexJdbc = resultSet.getString("bytes_cidade");

                System.out.println(
                        ">>> JDBC CIDADE: "
                        + cidadeJdbc
                );

                System.out.println(
                        ">>> JDBC HEX: "
                        + hexJdbc
                );

                System.out.println(
                        ">>> JDBC BYTES: "
                        + java.util.Arrays.toString(
                                cidadeJdbc.getBytes(
                                        java.nio.charset.StandardCharsets.UTF_8
                                )
                        )
                );
            }

        } catch (Exception erro) {

            System.out.println(
                    ">>> ERRO TESTE JDBC CIDADE: "
                    + erro.getMessage()
            );
        }

        return vagas.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    // =========================
    // BUSCAR VAGA POR ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<VagaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return vagaRepository.findById(id)
                .map(vaga ->
                        ResponseEntity.ok(
                                converterParaDTO(vaga)
                        ))
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .build());
    }

    // =========================
    // LISTAR COMPETÊNCIAS DA VAGA
    // =========================

    @GetMapping("/{id}/competencias")
    public ResponseEntity<List<CompetenciaResponseDTO>> listarCompetencias(
            @PathVariable Long id) {

        if (!vagaRepository.existsById(id)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        List<VagaCompetencia> relacionamentos =
                vagaCompetenciaRepository.findByVagaId(id);

        List<Long> competenciaIds = new ArrayList<>();

        for (VagaCompetencia relacionamento : relacionamentos) {

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

    // =========================
    // CRIAR VAGA
    // =========================

    @PostMapping
    public ResponseEntity<VagaResponseDTO> criar(
            @Valid @RequestBody VagaRequestDTO dto) {

        Vaga vaga = vagaService.criar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(converterParaDTO(vaga));
    }

    // =========================
    // ATUALIZAR VAGA
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<VagaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VagaUpdateDTO dto) {

        try {

            Vaga vaga = vagaService.atualizar(id, dto);

            return ResponseEntity.ok(
                    converterParaDTO(vaga)
            );

        } catch (IllegalArgumentException erro) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    // =========================
    // EXCLUIR VAGA
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        try {

            vagaService.excluir(id);

            return ResponseEntity
                    .noContent()
                    .build();

        } catch (IllegalArgumentException erro) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    // =========================
    // CONVERTER ENTITY → DTO
    // =========================

    private VagaResponseDTO converterParaDTO(Vaga vaga) {

        return new VagaResponseDTO(
                vaga.getId(),
                vaga.getTitulo(),
                vaga.getEmpresa(),
                vaga.getDescricao(),
                vaga.getCidade(),
                vaga.getModalidade(),
                vaga.getSalario()
        );
    }
}