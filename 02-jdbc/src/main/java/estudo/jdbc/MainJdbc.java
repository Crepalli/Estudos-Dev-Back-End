package estudo.jdbc;

import estudo.Aluno;
import estudo.AlunoService;

// Demonstra o ciclo completo de CRUD. Cada execução limpa somente o aluno demonstrativo.
public class MainJdbc {
    public static void main(String[] args) {
        String url = args.length > 0 ? args[0] : "jdbc:h2:file:./dados/escola";
        AlunoJdbcRepository repository = new AlunoJdbcRepository(url);
        AlunoService service = new AlunoService(repository); // Mudou o repositório, não o serviço.
        String matricula = "DEMO-JDBC";
        if (repository.buscar(matricula).isPresent()) service.remover(matricula);
        service.cadastrar(matricula, "Ana");
        service.editar(matricula, "Ana Maria");
        if (!service.buscar(matricula).getNome().equals("Ana Maria")) throw new AssertionError("Edição falhou");
        for (Aluno aluno : service.listar()) System.out.println(aluno);
        service.remover(matricula);
        if (repository.buscar(matricula).isPresent()) throw new AssertionError("Remoção falhou");
        System.out.println("CRUD JDBC verificado.");
    }
}
