package estudo.jdbc;

import estudo.Aluno;
import estudo.AlunoRepository;
import java.sql.*;
import java.util.*;

// O mesmo contrato da etapa 1 agora é implementado com SQL; o serviço continua independente do banco.
public final class AlunoJdbcRepository implements AlunoRepository {
    private final String url;

    public AlunoJdbcRepository(String url) {
        this.url = url; // Uma URL em memória serve para verificações; uma URL de arquivo mantém os dados.
        try (Connection conexao = conectar(); Statement comando = conexao.createStatement()) {
            comando.executeUpdate("CREATE TABLE IF NOT EXISTS aluno (matricula VARCHAR(30) PRIMARY KEY, nome VARCHAR(120) NOT NULL)");
        } catch (SQLException erro) { throw falha(erro); }
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, "sa", ""); // Banco local didático, sem console web exposto.
    }

    @Override
    public Optional<Aluno> buscar(String matricula) {
        try (Connection conexao = conectar();
             PreparedStatement comando = conexao.prepareStatement("SELECT matricula, nome FROM aluno WHERE matricula = ?")) {
            comando.setString(1, matricula); // Parâmetros separam dados de instruções SQL.
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? Optional.of(mapear(resultado)) : Optional.empty();
            }
        } catch (SQLException erro) { throw falha(erro); }
    }

    @Override
    public List<Aluno> listar() {
        List<Aluno> alunos = new ArrayList<>();
        try (Connection conexao = conectar();
             PreparedStatement comando = conexao.prepareStatement("SELECT matricula, nome FROM aluno ORDER BY matricula");
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) alunos.add(mapear(resultado)); // Cada linha vira um objeto Java.
            return alunos;
        } catch (SQLException erro) { throw falha(erro); }
    }

    @Override
    public void salvar(Aluno aluno) {
        // MERGE é uma operação específica do H2: insere ou atualiza pela chave informada.
        try (Connection conexao = conectar();
             PreparedStatement comando = conexao.prepareStatement("MERGE INTO aluno (matricula, nome) KEY(matricula) VALUES (?, ?)")) {
            comando.setString(1, aluno.getMatricula());
            comando.setString(2, aluno.getNome());
            comando.executeUpdate(); // Executa uma escrita, em vez de retornar uma tabela de resultados.
        } catch (SQLException erro) { throw falha(erro); }
    }

    @Override
    public void remover(String matricula) {
        try (Connection conexao = conectar();
             PreparedStatement comando = conexao.prepareStatement("DELETE FROM aluno WHERE matricula = ?")) {
            comando.setString(1, matricula);
            comando.executeUpdate();
        } catch (SQLException erro) { throw falha(erro); }
    }

    private Aluno mapear(ResultSet linha) throws SQLException {
        return new Aluno(linha.getString("matricula"), linha.getString("nome"));
    }

    private IllegalStateException falha(SQLException erro) {
        return new IllegalStateException("Não foi possível acessar o banco.", erro); // Preserva a causa para depuração.
    }
}
