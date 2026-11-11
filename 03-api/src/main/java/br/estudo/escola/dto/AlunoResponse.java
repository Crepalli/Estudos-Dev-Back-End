package br.estudo.escola.dto;

import br.estudo.escola.model.Aluno;
import br.estudo.escola.model.Endereco;

// A resposta explícita evita devolver automaticamente novos campos internos que forem adicionados à entidade.
public record AlunoResponse(Long id, String matricula, String nome, String email, Endereco endereco) {
    public static AlunoResponse from(Aluno aluno) {
        return new AlunoResponse(aluno.getId(), aluno.getMatricula(), aluno.getNome(), aluno.getEmail(), aluno.getEndereco());
    }
}
