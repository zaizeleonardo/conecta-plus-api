package com.conecta.conect.plus.dto;

import java.time.LocalDateTime;

public class CandidaturaCandidatoDTO {

    private Long id;
    private Long usuarioId;
    private Long vagaId;

    private String nome;
    private String email;
    private String telefone;
    private String cidade;

    private LocalDateTime dataCandidatura;
    private String status;

    public CandidaturaCandidatoDTO() {
    }

    public CandidaturaCandidatoDTO(
            Long id,
            Long usuarioId,
            Long vagaId,
            String nome,
            String email,
            String telefone,
            String cidade,
            LocalDateTime dataCandidatura,
            String status) {

        this.id = id;
        this.usuarioId = usuarioId;
        this.vagaId = vagaId;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cidade = cidade;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
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