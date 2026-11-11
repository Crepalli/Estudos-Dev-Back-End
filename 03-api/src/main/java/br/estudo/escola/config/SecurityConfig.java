package br.estudo.escola.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@Configuration // Reúne objetos que precisam ser construídos e disponibilizados ao restante da aplicação.
public class SecurityConfig {
    @Bean
    SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Autenticação usa Bearer explícito, sem cookies de sessão.
                .sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(rotas -> rotas
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()) // A regra padrão exige token, inclusive para consultar CEP.
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> {})) // Spring valida assinatura e expiração.
                .build();
    }

    @Bean PasswordEncoder senhas() { return new BCryptPasswordEncoder(); }

    @Bean
    UserDetailsService usuarios(PasswordEncoder encoder, @Value("${estudo.usuario}") String usuario,
                                @Value("${estudo.senha}") String senha) {
        if (senha.length() < 12 || senha.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("ESTUDO_SENHA deve ter ao menos 12 caracteres e no máximo 72 bytes.");
        }
        return new InMemoryUserDetailsManager(User.withUsername(usuario)
                .password(encoder.encode(senha)).roles("ESTUDANTE").build()); // Guarda hash, não a senha original.
    }

    @Bean
    SecretKey chave() throws NoSuchAlgorithmException {
        KeyGenerator gerador = KeyGenerator.getInstance("HmacSHA256");
        gerador.init(256);
        return gerador.generateKey(); // Laboratório: reiniciar invalida tokens; não suporta múltiplas instâncias.
    }

    @Bean JwtEncoder emissor(SecretKey chave) { return new NimbusJwtEncoder(new ImmutableSecret<>(chave)); }

    @Bean
    JwtDecoder decodificador(SecretKey chave) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(chave).macAlgorithm(MacAlgorithm.HS256).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer("escola-comentada")); // Valida também quem emitiu.
        return decoder;
    }
}
