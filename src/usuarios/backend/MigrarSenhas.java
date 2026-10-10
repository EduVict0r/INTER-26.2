package usuarios.backend;

import compartilhado.backend.Conexao;
import compartilhado.backend.SenhaUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MigrarSenhas {

    public static void main(String[] args) {

        atualizarSenha("EwertonS@CentralD.com", "1234");
    }

    public static void atualizarSenha(
            String login,
            String senhaPura
    ) {

        String hash =
                SenhaUtil.gerarHash(senhaPura);

        String sql = """
                UPDATE funcionario
                SET senha_funcionario = ?
                WHERE email_funcionario = ?
                """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            comando.setString(1, hash);
            comando.setString(2, login);

            int linhasAlteradas =
                    comando.executeUpdate();

            if (linhasAlteradas > 0) {

                System.out.println(
                        "Senha de "
                                + login
                                + " convertida para hash com sucesso!"
                );

            } else {

                System.out.println(
                        "Funcionário com login "
                                + login
                                + " não encontrado."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao atualizar senha de "
                            + login
            );

            e.printStackTrace();
        }
    }
}