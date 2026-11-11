import estudo.*;

// Verificação executável sem bibliotecas: testa regras do cadastro, não detalhes internos das classes.
public class Verificar {
    public static void main(String[] args) {
        AlunoService service = new AlunoService(new AlunoEmMemoriaRepository());
        service.cadastrar(" 001 ", " Ana ");
        conferir(service.buscar("001").getNome().equals("Ana"), "Normalização");
        deveFalhar(() -> service.cadastrar("001", "Outro")); // Não aceita identidade repetida.
        deveFalhar(() -> service.cadastrar("002", " ")); // Não aceita nome vazio.
        deveFalhar(() -> service.cadastrar(" ", "João"));
        service.editar("001", "Ana Maria");
        conferir(service.buscar("001").getNome().equals("Ana Maria"), "Edição");
        deveFalhar(() -> service.editar("001", ""));
        conferir(service.buscar("001").getNome().equals("Ana Maria"), "Edição inválida preserva o nome");
        service.listar().clear();
        conferir(service.listar().size() == 1, "Lista não expõe a coleção interna");
        service.remover("001");
        conferir(service.listar().isEmpty(), "Remoção");
        deveFalhar(() -> service.buscar("001"));
        deveFalhar(() -> service.remover("001"));
        System.out.println("Todas as verificações do console passaram.");
    }

    private static void conferir(boolean condicao, String caso) {
        if (!condicao) throw new AssertionError(caso);
    }

    private static void deveFalhar(Runnable acao) {
        try { acao.run(); }
        catch (IllegalArgumentException esperado) { return; }
        throw new AssertionError("A entrada inválida foi aceita.");
    }
}
