package com.conecta.conect.plus.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarErrosValidacao(
            MethodArgumentNotValidException exception) {

        Map<String, String> erros = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(erro ->
                        erros.put(
                                erro.getField(),
                                erro.getDefaultMessage()
                        )
                );

        Map<String, Object> resposta = new HashMap<>();

        resposta.put("status", HttpStatus.BAD_REQUEST.value());
        resposta.put("mensagem", "Existem dados inválidos na requisição.");
        resposta.put("erros", erros);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resposta);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarErroRegraNegocio(
            IllegalArgumentException exception) {

        Map<String, Object> resposta = new HashMap<>();

        resposta.put("status", HttpStatus.CONFLICT.value());
        resposta.put("mensagem", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(resposta);
    }
}