package br.estudo.escola.controller;

import br.estudo.escola.service.ViaCepService;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/ceps") @Validated
public class CepController {
    private final ViaCepService service;
    public CepController(ViaCepService service) { this.service = service; }

    @GetMapping("/{cep}")
    public ViaCepService.CepResponse buscar(@PathVariable @Pattern(regexp = "[0-9]{8}") String cep) {
        return service.buscar(cep); // Somente oito dígitos chegam à integração externa.
    }
}
