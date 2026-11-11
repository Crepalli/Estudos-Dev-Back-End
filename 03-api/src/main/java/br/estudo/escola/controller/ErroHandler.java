package br.estudo.escola.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice // Centraliza a tradução de erros em respostas HTTP.
public class ErroHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ProblemDetail> campos(MethodArgumentNotValidException erro) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Confira os campos informados.");
        problema.setProperty("campos", erro.getBindingResult().getFieldErrors().stream()
                .map(campo -> campo.getField() + ": " + campo.getDefaultMessage()).toList());
        return ResponseEntity.badRequest().body(problema);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ProblemDetail> parametro(ConstraintViolationException erro) {
        return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Parâmetro inválido."));
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ProblemDetail> regra(ResponseStatusException erro) {
        return ResponseEntity.status(erro.getStatusCode())
                .body(ProblemDetail.forStatusAndDetail(erro.getStatusCode(), erro.getReason()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ProblemDetail> conflito(DataIntegrityViolationException erro) {
        // Não revela SQL, nomes de tabelas ou mensagens internas do driver ao cliente.
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Dados em conflito com um cadastro existente."));
    }
}
