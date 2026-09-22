package com.conecta.conect.plus.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VagaUpdateDTO {

    @NotBlank(message = "O título é obrigatório.")
    private String titulo;

    @NotBlank(message = "A empresa é obrigatória.")
    private String empresa;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @NotBlank(message = "A cidade é obrigatória.")
    private String cidade;

    @NotBlank(message = "A modalidade é obrigatória.")
    private String modalidade;

    @NotNull(message = "O salário é obrigatório.")
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "O salário não pode ser negativo."
    )
    private BigDecimal salario;

    public VagaUpdateDTO() {
    }

    public VagaUpdateDTO(
            String titulo,
            String empresa,
            String descricao,
            String cidade,
            String modalidade,
            BigDecimal salario) {

        this.titulo = titulo;
        this.empresa = empresa;
        this.descricao = descricao;
        this.cidade = cidade;
        this.modalidade = modalidade;
        this.salario = salario;
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