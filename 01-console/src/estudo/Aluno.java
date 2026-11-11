package estudo;

// O modelo representa os dados de um aluno, sem ler teclado ou acessar banco.
public final class Aluno {
    private final String matricula; // A matrícula identifica o aluno e não muda após a criação.
    private String nome; // private impede alterações diretas feitas por outras classes.

    public Aluno(String matricula, String nome) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a matrícula."); // Interrompe uma criação inválida.
        }
        this.matricula = matricula.trim(); // this diferencia o atributo do parâmetro recebido.
        alterarNome(nome); // Reaproveita a mesma validação usada durante uma edição.
    }

    public String getMatricula() { return matricula; } // Permite consultar, mas não alterar a identidade.
    public String getNome() { return nome; }

    public void alterarNome(String nome) {
        if (nome == null || nome.trim().length() < 2) {
            throw new IllegalArgumentException("O nome deve ter pelo menos dois caracteres.");
        }
        this.nome = nome.trim(); // Só altera o estado depois de validar a entrada.
    }

    @Override // Indica que estamos substituindo um método herdado de Object.
    public String toString() {
        return matricula + " - " + nome; // Define como o aluno aparece no console.
    }
}
