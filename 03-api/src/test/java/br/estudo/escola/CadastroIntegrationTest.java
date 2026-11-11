package br.estudo.escola;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Testa HTTP + autenticação real + serviço + banco em memória, sem depender de ViaCEP.
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:teste;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop", "estudo.senha=SomenteParaTeste123!"})
@AutoConfigureMockMvc
class CadastroIntegrationTest {
    @Autowired MockMvc http;
    @Autowired ObjectMapper json;

    @Test
    void protegeRotasEExecutaCrud() throws Exception {
        http.perform(get("/api/alunos")).andExpect(status().isUnauthorized());
        http.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuario\":\"estudante\",\"senha\":\"incorreta\"}")).andExpect(status().isUnauthorized());
        String login = http.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuario\":\"estudante\",\"senha\":\"SomenteParaTeste123!\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String bearer = "Bearer " + json.readTree(login).get("accessToken").asText();
        String corpo = """
                {"matricula":"001","nome":"Ana","email":"ana@example.com",
                 "endereco":{"cep":"01001000","logradouro":"Praça da Sé","numero":"1","cidade":"São Paulo","uf":"SP"}}
                """;
        String criado = http.perform(post("/api/alunos").header("Authorization", bearer)
                .contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"))
                .andReturn().getResponse().getContentAsString();
        long id = json.readTree(criado).get("id").asLong();
        http.perform(post("/api/alunos").header("Authorization", bearer).contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isConflict());
        http.perform(post("/api/alunos").header("Authorization", bearer).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        http.perform(post("/api/alunos").header("Authorization", bearer).contentType(MediaType.APPLICATION_JSON)
                .content(corpo.replace("Ana", " A ").replace("001", "002"))).andExpect(status().isBadRequest());
        http.perform(get("/api/alunos?tamanho=101").header("Authorization", bearer)).andExpect(status().isBadRequest());
        http.perform(get("/api/alunos").header("Authorization", bearer)).andExpect(jsonPath("$.content[0].matricula").value("001"));
        http.perform(put("/api/alunos/" + id).header("Authorization", bearer).contentType(MediaType.APPLICATION_JSON)
                .content(corpo.replace("Ana", "Ana Maria"))).andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Ana Maria"));
        http.perform(put("/api/alunos/" + id).header("Authorization", bearer).contentType(MediaType.APPLICATION_JSON)
                .content(corpo.replace("001", "002"))).andExpect(status().isBadRequest());
        http.perform(delete("/api/alunos/" + id).header("Authorization", bearer)).andExpect(status().isNoContent());
        http.perform(get("/api/alunos/" + id).header("Authorization", bearer)).andExpect(status().isNotFound());
        http.perform(delete("/api/alunos/" + id).header("Authorization", bearer)).andExpect(status().isNotFound());
        http.perform(get("/api/alunos").header("Authorization", "Bearer invalido")).andExpect(status().isUnauthorized());
    }
}
