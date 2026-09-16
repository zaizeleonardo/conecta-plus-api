package com.conecta.conect.plus.dto;

import java.math.BigDecimal;

public class VagaResponseDTO {

    private Long id;
    private String titulo;
    private String empresa;
    private String descricao;
    private String cidade;
    private String modalidade;
    private BigDecimal salario;

    public VagaResponseDTO() {
    }

    public VagaResponseDTO(
            Long id,
            String titulo,
            String empresa,
            String descricao,
            String cidade,
            String modalidade,
            BigDecimal salario) {

        this.id = id;
        this.titulo = titulo;
        this.empresa = empresa;
        this.descricao = descricao;
        this.cidade = cidade;
        this.modalidade = modalidade;
        this.salario = salario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }
}