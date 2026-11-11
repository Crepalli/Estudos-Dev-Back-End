package estudo;

import java.util.List;
import java.util.Optional;

// Interface é um contrato: o serviço sabe quais operações existem, sem saber como são feitas.
public interface AlunoRepository {
    Optional<Aluno> buscar(String matricula); // Optional representa uma busca que pode não encontrar nada.
    List<Aluno> listar();
    void salvar(Aluno aluno);
    void remover(String matricula);
}
