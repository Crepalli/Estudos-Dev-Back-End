package br.estudo.escola.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record AlunoRequest(
        @NotBlank @Pattern(regexp = "[A-Za-z0-9-]{1,30}") String matricula,
        @NotBlank @Size(min = 2, max = 120) String nome,
        @NotBlank @Email @Size(max = 160) String email,
        @NotNull @Valid EnderecoRequest endereco) {} // @Valid também verifica os campos do objeto interno.
