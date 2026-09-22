package com.conecta.conect.plus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CursoUpdateDTO {

    @NotBlank(message = "O nome do curso é obrigatório.")
    @Size(max = 150, message = "O nome do curso deve ter no máximo 150 caracteres.")
    private String nome;

    @NotBlank(message = "A plataforma é obrigatória.")
    @Size(max = 100, message = "A plataforma deve ter no máximo 100 caracteres.")
    private String plataforma;

    @NotBlank(message = "A URL é obrigatória.")
    @Size(max = 500, message = "A URL deve ter no máximo 500 caracteres.")
    private String url;

    public CursoUpdateDTO() {
    }

    public CursoUpdateDTO(
            String nome,
            String plataforma,
            String url) {

        this.nome = nome;
        this.plataforma = plataforma;
        this.url = url;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}