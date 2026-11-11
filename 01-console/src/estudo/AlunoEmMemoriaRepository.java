package estudo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Esta implementação perde os dados ao encerrar; essa limitação é intencional na primeira etapa.
public final class AlunoEmMemoriaRepository implements AlunoRepository {
    private final Map<String, Aluno> alunos = new LinkedHashMap<String, Aluno>(); // Chave: matrícula; valor: aluno.

    @Override
    public Optional<Aluno> buscar(String matricula) {
        return Optional.ofNullable(alunos.get(matricula)); // Uma chave ausente produz Optional.empty().
    }

    @Override
    public List<Aluno> listar() {
        return new ArrayList<Aluno>(alunos.values()); // Copia a coleção para não expor a estrutura interna.
    }

    @Override
    public void salvar(Aluno aluno) {
        alunos.put(aluno.getMatricula(), aluno); // A mesma chave atualiza; uma chave nova insere.
    }

    @Override
    public void remover(String matricula) {
        alunos.remove(matricula);
    }
}
