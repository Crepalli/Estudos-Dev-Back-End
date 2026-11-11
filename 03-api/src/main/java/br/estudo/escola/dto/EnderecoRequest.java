package br.estudo.escola.dto;

import br.estudo.escola.model.Endereco;
import jakarta.validation.constraints.*;

// DTO define o formato aceito na API; o cliente não envia diretamente uma entidade JPA.
public record EnderecoRequest(
        @NotBlank @Pattern(regexp = "[0-9]{8}") String cep,
        @NotBlank @Size(max = 150) String logradouro,
        @NotBlank @Size(max = 20) String numero,
        @NotBlank @Size(max = 120) String cidade,
        @NotBlank @Pattern(regexp = "[A-Za-z]{2}") String uf) {
    public Endereco toModel() {
        return new Endereco(cep, logradouro.trim(), numero.trim(), cidade.trim(), uf.toUpperCase(java.util.Locale.ROOT));
    }
}
