package estudo;

import java.util.List;

// Serviço reúne as regras do cadastro. Ele não conhece Scanner nem detalhes de armazenamento.
public final class AlunoService {
    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository; // Injeção por construtor permite trocar memória por outra implementação.
    }

    public Aluno cadastrar(String matricula, String nome) {
        Aluno aluno = new Aluno(matricula, nome); // O próprio modelo verifica os dados obrigatórios.
        if (repository.buscar(aluno.getMatricula()).isPresent()) {
            throw new IllegalArgumentException("Matrícula já cadastrada.");
        }
        repository.salvar(aluno); // Só persiste depois que todas as regras foram atendidas.
        return aluno;
    }

    public List<Aluno> listar() { return repository.listar(); }

    public Aluno buscar(String matricula) {
        String chave = matricula == null ? "" : matricula.trim();
        return repository.buscar(chave)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));
    }

    public void editar(String matricula, String nome) {
        Aluno aluno = buscar(matricula); // Reutiliza a regra de aluno inexistente.
        aluno.alterarNome(nome);
        repository.salvar(aluno);
    }

    public void remover(String matricula) {
        Aluno aluno = buscar(matricula); // Uma remoção inexistente deve ser informada ao usuário.
        repository.remover(aluno.getMatricula());
    }
}
