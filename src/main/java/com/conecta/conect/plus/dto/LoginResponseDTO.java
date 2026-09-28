package com.conecta.conect.plus.dto;

public class LoginResponseDTO {

    private UsuarioResponseDTO usuario;
    private String token;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(
            UsuarioResponseDTO usuario,
            String token) {

        this.usuario = usuario;
        this.token = token;
    }

    public UsuarioResponseDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResponseDTO usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}