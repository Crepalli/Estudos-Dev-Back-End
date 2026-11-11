package br.estudo.escola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Ativa configuração automática e procura componentes neste pacote e subpacotes.
public class EscolaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EscolaApplication.class, args); // Inicia servidor HTTP e contexto de dependências.
    }
}
