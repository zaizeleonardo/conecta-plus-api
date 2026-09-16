package com.conecta.conect.plus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conecta.conect.plus.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}