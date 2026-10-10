package compartilhado.frontend;

import dashboard.frontend.TelaDashboard;
import estoque.frontend.TelaEstoque;
import login.frontend.TelaLogin;
import usuarios.backend.Funcionario;
import usuarios.frontend.TelaCadastroFuncionario;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

// Janela aberta depois do login: menu lateral, barra superior e a página atual.
public class JanelaPrincipal extends JFrame {

    private final Funcionario usuario;

    private final CardLayout paginas = new CardLayout();
    private final JPanel conteudo = new JPanel(paginas);

    private final MenuLateral menu = new MenuLateral(this::navegar);
    private final BarraSuperior barraSuperior;
    private final TelaDashboard dashboard;
    private final TelaEstoque estoque = new TelaEstoque();

    public JanelaPrincipal(Funcionario usuario) {
        this.usuario = usuario;

        setTitle("Central Descartáveis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        setSize(1366, 820);
        setLocationRelativeTo(null);

        barraSuperior = new BarraSuperior(usuario);
        dashboard = new TelaDashboard(usuario);

        if (isAdministrador()) {
            barraSuperior.adicionarOpcaoMenu("Cadastrar funcionário", this::abrirCadastroFuncionario);
        }
        barraSuperior.adicionarOpcaoMenu("Sair", this::sair);

        // A busca filtra a tabela de estoque; Enter leva até ela.
        barraSuperior.aoDigitarBusca(estoque::filtrar);
        barraSuperior.aoConfirmarBusca(() -> navegar(MenuLateral.ESTOQUE));

        dashboard.getVerTodosAlertas().addActionListener(e -> navegar(MenuLateral.ESTOQUE));
        dashboard.getVerTodosMaisVendidos().addActionListener(e -> emDesenvolvimento(MenuLateral.VENDAS));
        estoque.aoClicarVerTodosProdutos(barraSuperior::limparBusca);

        conteudo.setOpaque(false);
        conteudo.add(dashboard, MenuLateral.DASHBOARD);
        conteudo.add(estoque, MenuLateral.ESTOQUE);

        JPanel area = new JPanel(new BorderLayout(0, 22));
        area.setBackground(Tema.FUNDO);
        area.setBorder(BorderFactory.createEmptyBorder(18, 22, 22, 22));
        area.add(barraSuperior, BorderLayout.NORTH);
        area.add(conteudo, BorderLayout.CENTER);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.add(menu, BorderLayout.WEST);
        raiz.add(area, BorderLayout.CENTER);
        setContentPane(raiz);

        navegar(MenuLateral.DASHBOARD);
    }

    private void navegar(String pagina) {
        switch (pagina) {
            case MenuLateral.DASHBOARD -> {
                mostrar(pagina);
                dashboard.atualizar();
            }
            case MenuLateral.ESTOQUE -> {
                mostrar(pagina);
                estoque.atualizar();
            }
            case MenuLateral.USUARIOS -> abrirCadastroFuncionario();
            default -> emDesenvolvimento(pagina);
        }
    }

    private void mostrar(String pagina) {
        paginas.show(conteudo, pagina);
        menu.setAtivo(pagina);
    }

    private void abrirCadastroFuncionario() {
        if (!isAdministrador()) {
            JOptionPane.showMessageDialog(this,
                    "Seu perfil não tem permissão para cadastrar funcionários.",
                    "Acesso negado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        TelaCadastroFuncionario tela = new TelaCadastroFuncionario(usuario);
        tela.setLocationRelativeTo(this);
        tela.setVisible(true);
    }

    private void sair() {
        dispose();
        new TelaLogin().setVisible(true);
    }

    private void emDesenvolvimento(String modulo) {
        JOptionPane.showMessageDialog(this,
                "O módulo " + modulo + " ainda está em desenvolvimento.",
                modulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isAdministrador() {
        return "ADMINISTRADOR".equalsIgnoreCase(usuario.getPerfil_funcionario());
    }
}
