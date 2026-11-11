package br.estudo.escola.controller;

import br.estudo.escola.dto.*;
import br.estudo.escola.service.AlunoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController @RequestMapping("/api/alunos") @Validated
public class AlunoController {
    private final AlunoService service;
    public AlunoController(AlunoService service) { this.service = service; }

    @PostMapping // POST cria um recurso; 201 e Location informam onde ele pode ser consultado.
    public ResponseEntity<AlunoResponse> criar(@Valid @RequestBody AlunoRequest dados) {
        AlunoResponse aluno = service.criar(dados);
        return ResponseEntity.created(URI.create("/api/alunos/" + aluno.id())).body(aluno);
    }

    @GetMapping
    public Page<AlunoResponse> listar(@RequestParam(defaultValue = "0") @Min(0) int pagina,
                                      @RequestParam(defaultValue = "10") @Min(1) @Max(100) int tamanho) {
        return service.listar(pagina, tamanho);
    }

    @GetMapping("/{id}")
    public AlunoResponse buscar(@PathVariable @Positive Long id) { return service.buscar(id); }

    @PutMapping("/{id}")
    public AlunoResponse editar(@PathVariable @Positive Long id, @Valid @RequestBody AlunoRequest dados) {
        return service.editar(id, dados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable @Positive Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build(); // 204 significa sucesso sem corpo de resposta.
    }
}
