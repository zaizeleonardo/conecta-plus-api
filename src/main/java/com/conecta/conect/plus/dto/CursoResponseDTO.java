package com.conecta.conect.plus.dto;

public class CursoResponseDTO {

    private Long id;
    private String nome;
    private String plataforma;
    private String url;

    public CursoResponseDTO() {
    }

    public CursoResponseDTO(
            Long id,
            String nome,
            String plataforma,
            String url) {

        this.id = id;
        this.nome = nome;
        this.plataforma = plataforma;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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