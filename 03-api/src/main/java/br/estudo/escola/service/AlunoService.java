package br.estudo.escola.service;

import br.estudo.escola.dto.*;
import br.estudo.escola.model.Aluno;
import br.estudo.escola.repository.AlunoRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service // Registra esta classe para que Spring possa injetá-la no controller.
@Transactional // Escritas concluem juntas; uma exceção provoca rollback.
public class AlunoService {
    private final AlunoRepository repository;
    public AlunoService(AlunoRepository repository) { this.repository = repository; }

    public AlunoResponse criar(AlunoRequest dados) {
        if (repository.existsByMatricula(dados.matricula())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Matrícula já cadastrada.");
        }
        Aluno aluno = new Aluno(dados.matricula(), dados.nome().trim(), dados.email().trim(), dados.endereco().toModel());
        return AlunoResponse.from(repository.saveAndFlush(aluno)); // flush revela violações de restrição antes da resposta.
    }

    @Transactional(readOnly = true)
    public Page<AlunoResponse> listar(int pagina, int tamanho) {
        return repository.findAll(PageRequest.of(pagina, tamanho, Sort.by("nome").and(Sort.by("id"))))
                .map(AlunoResponse::from); // Paginação impede carregar o cadastro inteiro em uma única resposta.
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscar(Long id) { return AlunoResponse.from(encontrar(id)); }

    public AlunoResponse editar(Long id, AlunoRequest dados) {
        Aluno aluno = encontrar(id);
        if (!aluno.getMatricula().equals(dados.matricula())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A matrícula não pode ser alterada.");
        }
        aluno.atualizar(dados.nome().trim(), dados.email().trim(), dados.endereco().toModel());
        return AlunoResponse.from(repository.saveAndFlush(aluno));
    }

    public void remover(Long id) { repository.delete(encontrar(id)); }

    private Aluno encontrar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado."));
    }
}
