package compartilhado.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    public static Connection conectar() throws SQLException {
        return conectar(ConfiguracaoBanco.atual());
    }

    public static Connection conectar(ConfiguracaoBanco configuracao) throws SQLException {

        // Deixa o login falhar rápido em vez de travar se o MySQL não responder.
        DriverManager.setLoginTimeout(5);

        return DriverManager.getConnection(
                configuracao.url(),
                configuracao.usuario(),
                configuracao.senha()
        );
    }

    // Abre e fecha uma conexão só para saber se o acesso está funcionando.
    public static void testar(ConfiguracaoBanco configuracao) throws SQLException {
        try (Connection conexao = conectar(configuracao)) {
            conexao.isValid(5);
        }
    }
}
