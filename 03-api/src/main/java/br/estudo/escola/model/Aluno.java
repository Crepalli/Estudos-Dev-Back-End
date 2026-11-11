package br.estudo.escola.model;

import jakarta.persistence.*;

@Entity // Mapeia esta classe para uma tabela.
@Table(name = "alunos")
public class Aluno {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // O banco gera a chave técnica.
    private Long id;
    @Column(nullable = false, unique = true, length = 30) // O banco também protege a unicidade, inclusive em requisições simultâneas.
    private String matricula;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(nullable = false, length = 160)
    private String email;
    @Embedded // Composição: endereço faz parte do estado do aluno.
    private Endereco endereco;

    protected Aluno() {}

    public Aluno(String matricula, String nome, String email, Endereco endereco) {
        this.matricula = matricula;
        atualizar(nome, email, endereco);
    }

    public void atualizar(String nome, String email, Endereco endereco) {
        this.nome = nome; this.email = email; this.endereco = endereco;
    }

    public Long getId() { return id; }
    public String getMatricula() { return matricula; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public Endereco getEndereco() { return endereco; }
}
