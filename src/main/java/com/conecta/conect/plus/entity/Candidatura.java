package com.conecta.conect.plus.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "candidaturas",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_usuario_vaga",
            columnNames = {"usuario_id", "vaga_id"}
        )
    }
)
public class Candidatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "vaga_id", nullable = false)
    private Long vagaId;

    @Column(name = "data_candidatura", nullable = false)
    private LocalDateTime dataCandidatura;

    @Column(nullable = false)
    private String status;

    public Candidatura() {
    }

    public Candidatura(
            Long usuarioId,
            Long vagaId,
            LocalDateTime dataCandidatura,
            String status) {

        this.usuarioId = usuarioId;
        this.vagaId = vagaId;
        this.dataCandidatura = dataCandidatura;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getVagaId() {
        return vagaId;
    }

    public void setVagaId(Long vagaId) {
        this.vagaId = vagaId;
    }

    public LocalDateTime getDataCandidatura() {
        return dataCandidatura;
    }

    public void setDataCandidatura(LocalDateTime dataCandidatura) {
        this.dataCandidatura = dataCandidatura;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}