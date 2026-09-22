package com.conecta.conect.plus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioUpdateDTO {

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    @NotBlank(message = "A cidade é obrigatória.")
    private String cidade;

    @NotBlank(message = "O objetivo profissional é obrigatório.")
    private String objetivoProfissional;

    public UsuarioUpdateDTO() {
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

    public String getObjetivoProfissional() {
        return objetivoProfissional;
    }

    public void setObjetivoProfissional(String objetivoProfissional) {
        this.objetivoProfissional = objetivoProfissional;
    }
}