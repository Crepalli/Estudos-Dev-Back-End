package br.estudo.escola.service;

import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ViaCepService {
    public record CepResponse(String cep, String logradouro, String localidade, String uf, Boolean erro) {}
    private final RestClient cliente;

    public ViaCepService() {
        SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(3000); // Evita aguardar indefinidamente por conexão externa.
        fabrica.setReadTimeout(3000);
        cliente = RestClient.builder().baseUrl("https://viacep.com.br").requestFactory(fabrica).build();
    }

    public CepResponse buscar(String cep) {
        try {
            CepResponse resposta = cliente.get().uri("/ws/{cep}/json/", cep)
                    .retrieve().body(CepResponse.class); // Converte JSON em objeto; não separa texto manualmente.
            if (resposta == null || Boolean.TRUE.equals(resposta.erro())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP não encontrado.");
            }
            return resposta;
        } catch (RestClientException erro) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Consulta de CEP indisponível. Preencha o endereço manualmente.");
        }
    }
}
