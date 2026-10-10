package usuarios.backend;

import compartilhado.backend.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FuncionarioDAO {

    public Funcionario buscarPorLogin(String login) throws SQLException {

        String sql = """
                SELECT
                    id_funcionario,
                    nome_funcionario,
                    senha_funcionario,
                    email_funcionario,
                    perfil_funcionario,
                    status_funcionario
                FROM funcionario
                WHERE email_funcionario = ?
                """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {

            comando.setString(1, login);

            try (ResultSet resultado = comando.executeQuery()) {

                if (resultado.next()) {

                    String nome =
                            resultado.getString("nome_funcionario");

                    String email =
                            resultado.getString("email_funcionario");

                    String senha =
                            resultado.getString("senha_funcionario");


                    Funcionario funcionario =
                            new Funcionario(
                                    nome,
                                    email,
                                    senha
                                    );

                    funcionario.setId_funcionario(
                            resultado.getInt("id_funcionario")
                    );

                    funcionario.setPerfil_funcionario(
                            resultado.getString("perfil_funcionario")
                    );

                    funcionario.setStatus_funcionario(
                            resultado.getString("status_funcionario")
                    );

                    return funcionario;
                }
            }
        }

        // Erros de banco sobem para quem chamou, para não parecerem "senha inválida".
        return null;
    }
    public int cadastrar(Funcionario novo, int idAdministrador)
            throws SQLException {

        String sql = """
            INSERT INTO funcionario (
                nome_funcionario,
                senha_funcionario,
                email_funcionario,
                perfil_funcionario,
                status_funcionario,
                id_funcionario_cadastrou
            )
            VALUES (?, ?, ?, ?, 'ATIVO', ?)
            """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement comando = conexao.prepareStatement(
                        sql,
                        java.sql.Statement.RETURN_GENERATED_KEYS
                )
        ) {
            comando.setString(1, novo.getNome_funcionario());
            comando.setString(2, novo.getSenha_funcionario());
            comando.setString(3, novo.getEmail_funcionario());
            comando.setString(4, novo.getPerfil_funcionario());
            comando.setInt(5, idAdministrador);

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    return resultado.getInt(1);
                }
            }

            throw new SQLException(
                    "Cadastro realizado, mas o banco não retornou o ID."
            );
        }
    }
}