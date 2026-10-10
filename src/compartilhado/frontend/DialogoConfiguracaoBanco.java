package compartilhado.frontend;

import compartilhado.backend.ConfiguracaoBanco;
import compartilhado.backend.Conexao;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.IOException;
import java.sql.SQLException;

// Janela mostrada quando o programa não consegue se conectar ao MySQL.
public class DialogoConfiguracaoBanco extends JDialog {

    private final JTextField campoUrl = new JTextField(32);
    private final JTextField campoUsuario = new JTextField(32);
    private final JPasswordField campoSenha = new JPasswordField(32);
    private final JLabel mensagem = new JLabel(" ");

    private boolean configurado;

    private DialogoConfiguracaoBanco(String erroInicial) {
        super((Frame) null, "Conexão com o banco de dados", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ConfiguracaoBanco atual = ConfiguracaoBanco.atual();
        campoUrl.setText(atual.url());
        campoUsuario.setText(atual.usuario());
        campoSenha.setText(atual.senha());

        JLabel titulo = new JLabel("Não foi possível conectar ao MySQL");
        titulo.setFont(Tema.fonteNegrito(16));
        titulo.setForeground(Tema.TEXTO_TITULO);

        JLabel explicacao = new JLabel("<html>Confira os dados de acesso. Eles serão salvos em<br>"
                + ConfiguracaoBanco.ARQUIVO + "</html>");
        explicacao.setFont(Tema.fonte(12));
        explicacao.setForeground(Tema.TEXTO_SUAVE);

        JPanel topo = new JPanel(new BorderLayout(0, 6));
        topo.setOpaque(false);
        topo.add(titulo, BorderLayout.NORTH);
        topo.add(explicacao, BorderLayout.CENTER);

        JPanel campos = new JPanel(new GridLayout(0, 1, 0, 4));
        campos.setOpaque(false);
        campos.add(rotulo("URL (jdbc:mysql://servidor:porta/banco)"));
        campos.add(campoUrl);
        campos.add(rotulo("Usuário"));
        campos.add(campoUsuario);
        campos.add(rotulo("Senha"));
        campos.add(campoSenha);

        mensagem.setFont(Tema.fonte(12));
        mensagem.setForeground(Tema.VERMELHO);
        mostrarErro(erroInicial);

        BotaoArredondado sair = BotaoArredondado.secundario("Sair");
        sair.addActionListener(e -> dispose());

        BotaoArredondado conectar = BotaoArredondado.primario("Testar e salvar", null);
        conectar.addActionListener(e -> testarESalvar());
        getRootPane().setDefaultButton(conectar);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.setOpaque(false);
        botoes.add(sair);
        botoes.add(conectar);

        JPanel rodape = new JPanel(new BorderLayout(0, 12));
        rodape.setOpaque(false);
        rodape.add(mensagem, BorderLayout.NORTH);
        rodape.add(botoes, BorderLayout.SOUTH);

        JPanel conteudo = new JPanel(new BorderLayout(0, 18));
        conteudo.setBackground(Tema.BRANCO);
        conteudo.setBorder(BorderFactory.createEmptyBorder(22, 24, 20, 24));
        conteudo.add(topo, BorderLayout.NORTH);
        conteudo.add(campos, BorderLayout.CENTER);
        conteudo.add(rodape, BorderLayout.SOUTH);

        setContentPane(conteudo);
        pack();
        setLocationRelativeTo(null);
    }

    // Abre a janela e espera. Retorna true se a conexão foi configurada com sucesso.
    public static boolean configurar(String erroInicial) {
        DialogoConfiguracaoBanco dialogo = new DialogoConfiguracaoBanco(erroInicial);
        dialogo.setVisible(true);
        return dialogo.configurado;
    }

    private void testarESalvar() {
        ConfiguracaoBanco nova = new ConfiguracaoBanco(
                campoUrl.getText().trim(),
                campoUsuario.getText().trim(),
                new String(campoSenha.getPassword()));

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            Conexao.testar(nova);
            ConfiguracaoBanco.salvar(nova);
            configurado = true;
            dispose();
        } catch (SQLException erro) {
            mostrarErro(erro.getMessage());
        } catch (IOException erro) {
            mostrarErro("Conectou, mas não foi possível salvar o arquivo: " + erro.getMessage());
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void mostrarErro(String texto) {
        // Quebra mensagens longas do MySQL para caberem na janela.
        mensagem.setText(texto == null || texto.isBlank()
                ? " "
                : "<html><body style='width:320px'>" + texto + "</body></html>");
        pack();
    }

    private static JLabel rotulo(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(Tema.fonteNegrito(12));
        rotulo.setForeground(Tema.TEXTO_TITULO);
        return rotulo;
    }
}
