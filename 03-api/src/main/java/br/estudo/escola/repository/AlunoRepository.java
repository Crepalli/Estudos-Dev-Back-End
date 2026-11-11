package br.estudo.escola.repository;

import br.estudo.escola.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring cria a implementação em execução; Long é o tipo da chave primária.
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByMatricula(String matricula); // O nome do método descreve a consulta que será criada.
}
