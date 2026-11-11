package br.estudo.escola.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;

@RestController @RequestMapping("/auth")
public class AuthController {
    public record Login(@NotBlank @Size(max = 100) String usuario, @NotBlank @Size(max = 72) String senha) {}
    public record Token(String accessToken, String tokenType, long expiresIn) {}
    private final UserDetailsService usuarios;
    private final PasswordEncoder encoder;
    private final JwtEncoder emissor;

    public AuthController(UserDetailsService usuarios, PasswordEncoder encoder, JwtEncoder emissor) {
        this.usuarios = usuarios; this.encoder = encoder; this.emissor = emissor;
    }

    @PostMapping("/login")
    public Token login(@Valid @RequestBody Login dados) {
        UserDetails usuario;
        try { usuario = usuarios.loadUserByUsername(dados.usuario()); }
        catch (UsernameNotFoundException erro) { throw credenciaisInvalidas(); }
        if (!encoder.matches(dados.senha(), usuario.getPassword())) throw credenciaisInvalidas();
        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("escola-comentada")
                .subject(usuario.getUsername()).issuedAt(agora).expiresAt(agora.plusSeconds(900)).build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = emissor.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new Token(token, "Bearer", 900); // O cliente recebe apenas o token; a senha não é devolvida.
    }

    private ResponseStatusException credenciaisInvalidas() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos.");
    }
}
