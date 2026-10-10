package usuarios.frontend;

import usuarios.backend.Funcionario;
import usuarios.backend.FuncionarioService;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class TelaCadastroFuncionario extends JFrame {

    private final Funcionario administrador;
    private final FuncionarioService service = new FuncionarioService();

    private final JTextField campoNome = new JTextField(25);
    private final JTextField campoEmail = new JTextField(25);
    private final JPasswordField campoSenha = new JPasswordField(25);

    private final JComboBox<String> campoPerfil =
            new JComboBox<>(new String[]{
                    "VENDEDOR",
                    "ADMINISTRADOR"
            });

    private final JButton botaoCadastrar = new JButton("Cadastrar");

    public TelaCadastroFuncionario(Funcionario administrador) {
        this.administrador = administrador;

        setTitle("Cadastro de funcionário");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));
        formulario.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        formulario.add(new JLabel("Nome:"));
        formulario.add(campoNome);

        formulario.add(new JLabel("E-mail:"));
        formulario.add(campoEmail);

        formulario.add(new JLabel("Senha inicial:"));
        formulario.add(campoSenha);

        formulario.add(new JLabel("Perfil:"));
        formulario.add(campoPerfil);

        formulario.add(new JLabel(""));
        formulario.add(botaoCadastrar);

        add(formulario);

        botaoCadastrar.addActionListener(evento -> cadastrar());

        pack();
        setLocationRelativeTo(null);
    }

    private void cadastrar() {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        char[] senha = campoSenha.getPassword();
        String perfil = (String) campoPerfil.getSelectedItem();

        botaoCadastrar.setEnabled(false);

        // Executa o acesso ao banco e o hash sem travar a janela.
        SwingWorker<Integer, Void> tarefa = new SwingWorker<>() {

            @Override
            protected Integer doInBackground() throws Exception {
                try {
                    return service.cadastrar(
                            administrador,
                            nome,
                            email,
                            senha,
                            perfil
                    );
                } finally {
                    Arrays.fill(senha, '\0');
                }
            }

            @Override
            protected void done() {
                try {
                    int id = get();

                    JOptionPane.showMessageDialog(
                            TelaCadastroFuncionario.this,
                            "Funcionário cadastrado! ID: " + id
                    );

                    campoNome.setText("");
                    campoEmail.setText("");
                    campoPerfil.setSelectedIndex(0);
                    campoNome.requestFocusInWindow();

                } catch (InterruptedException erro) {
                    Thread.currentThread().interrupt();
                    mostrarErro("A operação foi interrompida.");

                } catch (ExecutionException erro) {
                    tratarErro(erro.getCause());

                } finally {
                    campoSenha.setText("");
                    botaoCadastrar.setEnabled(true);
                }
            }
        };

        tarefa.execute();
    }

    private void tratarErro(Throwable erro) {
        if (erro instanceof SecurityException
                || erro instanceof IllegalArgumentException) {

            mostrarErro(erro.getMessage());

        } else if (erro instanceof SQLException) {
            SQLException erroSQL = (SQLException) erro;

            if (erroSQL.getErrorCode() == 1062) {
                mostrarErro(
                        "Cadastro duplicado. Confira se o e-mail já está em uso."
                );
            } else {
                mostrarErro(
                        "Não foi possível concluir o cadastro no banco. "
                                + "Confira o console."
                );
                erroSQL.printStackTrace();
            }

        } else {
            mostrarErro("Ocorreu um erro inesperado. Confira o console.");
            erro.printStackTrace();
        }
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Não foi possível cadastrar",
                JOptionPane.ERROR_MESSAGE
        );
    }
}