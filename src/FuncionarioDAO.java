import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FuncionarioDAO {

    public Funcionario buscarPorLogin(String login) {

        String sql = """
                SELECT
                    id_funcionario,
                    nome_funcionario,
                    login_funcionario,
                    senha_funcionario,
                    email_funcionario,
                    perfil_funcionario,
                    status_funcionario
                FROM funcionario
                WHERE login_funcionario = ?
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

                    String loginBanco =
                            resultado.getString("login_funcionario");

                    Funcionario funcionario =
                            new Funcionario(
                                    nome,
                                    email,
                                    senha,
                                    loginBanco
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

        } catch (SQLException e) {

            System.out.println("Erro ao buscar funcionário.");
            e.printStackTrace();
        }

        return null;
    }
}