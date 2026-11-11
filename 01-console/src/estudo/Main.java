package estudo;

import java.util.Scanner;

// A entrada da aplicação monta as dependências e cuida apenas da interação com o usuário.
public final class Main {
    public static void main(String[] args) {
        AlunoService service = new AlunoService(new AlunoEmMemoriaRepository());
        try (Scanner teclado = new Scanner(System.in, "UTF-8")) { // Fecha o recurso ao terminar.
            boolean executando = true;
            while (executando && teclado.hasNextLine()) { // Repete até sair ou acabar a entrada.
                System.out.println("\n1 Cadastrar | 2 Listar | 3 Buscar | 4 Editar | 5 Remover | 0 Sair");
                String opcao = teclado.nextLine().trim(); // Lê tudo como texto para evitar problemas com nextInt().
                try {
                    switch (opcao) {
                        case "1":
                            service.cadastrar(ler(teclado, "Matrícula: "), ler(teclado, "Nome: "));
                            System.out.println("Aluno cadastrado.");
                            break; // Evita executar também o próximo case.
                        case "2":
                            if (service.listar().isEmpty()) System.out.println("Nenhum aluno cadastrado.");
                            for (Aluno aluno : service.listar()) System.out.println(aluno);
                            break;
                        case "3":
                            System.out.println(service.buscar(ler(teclado, "Matrícula: ")));
                            break;
                        case "4":
                            service.editar(ler(teclado, "Matrícula: "), ler(teclado, "Novo nome: "));
                            System.out.println("Aluno atualizado.");
                            break;
                        case "5":
                            service.remover(ler(teclado, "Matrícula: "));
                            System.out.println("Aluno removido.");
                            break;
                        case "0": executando = false; break;
                        default: System.out.println("Opção inválida.");
                    }
                } catch (IllegalArgumentException erro) {
                    System.out.println("Atenção: " + erro.getMessage()); // Um erro de entrada não derruba o menu.
                }
            }
        }
    }

    private static String ler(Scanner teclado, String mensagem) {
        System.out.print(mensagem);
        return teclado.hasNextLine() ? teclado.nextLine() : "";
    }
}
