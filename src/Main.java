import compartilhado.backend.ConfiguracaoBanco;
import compartilhado.backend.Conexao;
import compartilhado.frontend.DialogoConfiguracaoBanco;
import login.frontend.TelaLogin;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        // Texto suavizado nas telas desenhadas à mão.
        System.setProperty("awt.useSystemAAFontSettings", "on");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Se não conseguir, segue com o visual padrão do Java.
        }

        // Testa o banco antes de abrir as telas (fora da thread da interface).
        String erroConexao = testarConexao();

        SwingUtilities.invokeLater(() -> {
            if (erroConexao != null && !DialogoConfiguracaoBanco.configurar(erroConexao)) {
                System.exit(0);
            }
            new TelaLogin().setVisible(true);
        });
    }

    // Retorna null se conectou, ou a mensagem de erro.
    private static String testarConexao() {
        try {
            Conexao.testar(ConfiguracaoBanco.atual());
            return null;
        } catch (SQLException e) {
            System.out.println("Falha ao conectar no banco: " + e.getMessage());
            return e.getMessage();
        }
    }
}
