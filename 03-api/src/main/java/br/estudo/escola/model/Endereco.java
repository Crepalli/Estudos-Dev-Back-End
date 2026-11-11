package br.estudo.escola.model;

import jakarta.persistence.Embeddable;

@Embeddable // Os campos são armazenados na tabela do aluno: um endereço por aluno nesta versão.
public class Endereco {
    private String cep;
    private String logradouro;
    private String numero;
    private String cidade;
    private String uf;

    protected Endereco() {} // JPA precisa de um construtor sem argumentos.

    public Endereco(String cep, String logradouro, String numero, String cidade, String uf) {
        this.cep = cep; this.logradouro = logradouro; this.numero = numero;
        this.cidade = cidade; this.uf = uf;
    }

    public String getCep() { return cep; }
    public String getLogradouro() { return logradouro; }
    public String getNumero() { return numero; }
    public String getCidade() { return cidade; }
    public String getUf() { return uf; }
}
