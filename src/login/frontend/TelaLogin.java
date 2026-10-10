package login.frontend;

import compartilhado.frontend.BotaoArredondado;
import compartilhado.frontend.CampoComIcone;
import compartilhado.frontend.Icone;
import compartilhado.frontend.JanelaPrincipal;
import compartilhado.frontend.Logo;
import compartilhado.frontend.Tema;
import login.backend.AutenticacaoService;
import usuarios.backend.Funcionario;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

// Tela de login: painel azul com a marca à esquerda e formulário à direita.
public class TelaLogin extends JFrame {

    // Imagem opcional exportada do Figma (cesta com copos e pratos).
    // Caminho relativo à pasta desta classe (login/frontend/imagens).
    private static final String IMAGEM_PRODUTOS = "imagens/login-produtos.png";

    private final AutenticacaoService service = new AutenticacaoService();

    private final CampoComIcone campoEmail = CampoComIcone.texto(
            "Digite seu e-mail", new Icone(Icone.Tipo.EMAIL, 18, Tema.TEXTO_SUAVE));
    private final CampoComIcone campoSenha = CampoComIcone.senha(
            "Digite sua senha", new Icone(Icone.Tipo.CADEADO, 18, Tema.TEXTO_SUAVE));

    private final JLabel mensagemErro = new JLabel(" ");
    private final BotaoArredondado botaoEntrar = BotaoArredondado.primario(
            "Entrar", new Icone(Icone.Tipo.SETA_DIREITA, 16, Tema.TEXTO_TITULO));

    public TelaLogin() {
        setTitle("Central Descartáveis - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(960, 640));
        setSize(1200, 760);
        setLocationRelativeTo(null);

        JPanel raiz = new JPanel(new GridLayout(1, 2));
        raiz.add(new PainelMarca());
        raiz.add(criarLadoFormulario());
        setContentPane(raiz);

        botaoEntrar.addActionListener(e -> entrar());
        getRootPane().setDefaultButton(botaoEntrar);
    }

    private JComponent criarLadoFormulario() {
        JPanel lado = new JPanel(new GridBagLayout()) {
            // Mancha azul-clara no canto superior direito.
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int l = getWidth();
                Path2D mancha = new Path2D.Double();
                mancha.moveTo(l * 0.55, 0);
                mancha.curveTo(l * 0.62, 70, l * 0.8, 120, l, 110);
                mancha.lineTo(l, 0);
                mancha.closePath();
                g2.setColor(new Color(0xE6ECF5));
                g2.fill(mancha);
                g2.dispose();
            }
        };
        lado.setBackground(new Color(0xF8FAFD));

        JPanel formulario = new JPanel();
        formulario.setOpaque(false);
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));

        formulario.add(alinhar(criarBoasVindas()));
        formulario.add(Box.createVerticalStrut(40));

        formulario.add(alinhar(rotulo("E-mail")));
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(alinhar(limitarAltura(campoEmail)));
        formulario.add(Box.createVerticalStrut(22));

        formulario.add(alinhar(rotulo("Senha")));
        formulario.add(Box.createVerticalStrut(8));
        campoSenha.setComponenteDireita(criarBotaoMostrarSenha());
        formulario.add(alinhar(limitarAltura(campoSenha)));
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(alinhar(criarLinkEsqueceuSenha()));

        mensagemErro.setFont(Tema.fonte(12));
        mensagemErro.setForeground(Tema.VERMELHO);
        formulario.add(Box.createVerticalStrut(14));
        formulario.add(alinhar(mensagemErro));
        formulario.add(Box.createVerticalStrut(14));

        botaoEntrar.setPreferredSize(new Dimension(380, 44));
        formulario.add(alinhar(limitarAltura(botaoEntrar)));

        lado.add(formulario);
        return lado;
    }

    private JComponent criarBoasVindas() {
        JPanel cabecalho = new JPanel(new BorderLayout(14, 0));
        cabecalho.setOpaque(false);
        cabecalho.add(new JLabel(new Icone(Icone.Tipo.CUBO, 46, Tema.AMARELO_ESCURO, 2.6f)),
                BorderLayout.WEST);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Bem-vindo de volta!");
        titulo.setFont(Tema.fonteNegrito(26));
        titulo.setForeground(Tema.TEXTO_TITULO);

        JLabel subtitulo = new JLabel("Acesse sua conta para continuar");
        subtitulo.setFont(Tema.fonte(13));
        subtitulo.setForeground(Tema.TEXTO_SUAVE);

        textos.add(titulo);
        textos.add(subtitulo);
        cabecalho.add(textos, BorderLayout.CENTER);
        return cabecalho;
    }

    private JComponent criarBotaoMostrarSenha() {
        JPasswordField senha = (JPasswordField) campoSenha.getCampo();
        char caractereOculto = senha.getEchoChar();

        Icone olho = new Icone(Icone.Tipo.OLHO, 18, Tema.TEXTO_SUAVE);
        Icone olhoFechado = new Icone(Icone.Tipo.OLHO_FECHADO, 18, Tema.TEXTO_SUAVE);

        JButton botao = new JButton(olho);
        botao.setToolTipText("Mostrar senha");
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setFocusable(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.addActionListener(e -> {
            boolean oculta = senha.getEchoChar() != 0;
            senha.setEchoChar(oculta ? (char) 0 : caractereOculto);
            botao.setIcon(oculta ? olhoFechado : olho);
            botao.setToolTipText(oculta ? "Ocultar senha" : "Mostrar senha");
        });
        return botao;
    }

    private JComponent criarLinkEsqueceuSenha() {
        JLabel link = new JLabel("Esqueceu sua senha?");
        link.setFont(Tema.fonte(12));
        link.setForeground(Tema.AZUL_CLARO);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(TelaLogin.this,
                        "Peça a um administrador para redefinir sua senha.",
                        "Esqueceu sua senha?", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel linha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        linha.setOpaque(false);
        linha.add(link);
        return linha;
    }

    private void entrar() {
        String email = campoEmail.getCampo().getText().trim();
        char[] senha = ((JPasswordField) campoSenha.getCampo()).getPassword();

        if (email.isEmpty() || senha.length == 0) {
            mensagemErro.setText("Preencha e-mail e senha.");
            Arrays.fill(senha, '\0');
            return;
        }

        mensagemErro.setText(" ");
        botaoEntrar.setEnabled(false);
        botaoEntrar.setText("Entrando...");

        // O hash Argon2 e o banco ficam fora da thread da interface.
        new SwingWorker<Funcionario, Void>() {
            @Override
            protected Funcionario doInBackground() throws Exception {
                try {
                    return service.autenticar(email, new String(senha));
                } finally {
                    Arrays.fill(senha, '\0');
                }
            }

            @Override
            protected void done() {
                botaoEntrar.setEnabled(true);
                botaoEntrar.setText("Entrar");

                try {
                    Funcionario funcionario = get();

                    if (funcionario == null) {
                        mensagemErro.setText("E-mail ou senha inválidos.");
                        campoSenha.getCampo().setText("");
                        campoSenha.getCampo().requestFocusInWindow();
                        return;
                    }

                    dispose();
                    new JanelaPrincipal(funcionario).setVisible(true);

                } catch (InterruptedException erro) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException erro) {
                    Throwable causa = erro.getCause();
                    causa.printStackTrace();
                    String detalhe = causa instanceof SQLException ? causa.getMessage() : causa.toString();
                    JOptionPane.showMessageDialog(TelaLogin.this,
                            "Não foi possível conectar ao banco de dados.\n" + detalhe,
                            "Erro de conexão", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private static JLabel rotulo(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(Tema.fonteNegrito(13));
        rotulo.setForeground(Tema.TEXTO_TITULO);
        return rotulo;
    }

    private static JComponent alinhar(JComponent componente) {
        componente.setAlignmentX(LEFT_ALIGNMENT);
        return componente;
    }

    private static JComponent limitarAltura(JComponent componente) {
        int altura = Math.max(componente.getPreferredSize().height, 44);
        componente.setPreferredSize(new Dimension(380, altura));
        componente.setMaximumSize(new Dimension(Integer.MAX_VALUE, altura));
        return componente;
    }

    // Lado esquerdo: fundo azul com ondas, logo, slogan, imagem e destaques.
    private static class PainelMarca extends JPanel {

        PainelMarca() {
            super(new BorderLayout());
            setBackground(Tema.AZUL);
            setBorder(BorderFactory.createEmptyBorder(60, 56, 60, 56));

            JPanel topo = new JPanel();
            topo.setOpaque(false);
            topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));

            Logo logo = new Logo(34, Tema.BRANCO);
            logo.setAlignmentX(CENTER_ALIGNMENT);
            logo.setMaximumSize(logo.getPreferredSize());
            topo.add(logo);
            topo.add(Box.createVerticalStrut(12));

            JLabel slogan = new JLabel("<html>Mais praticidade para o "
                    + "<font color='#FFDD00'>seu negócio!</font></html>");
            slogan.setFont(Tema.fonteNegrito(17));
            slogan.setForeground(Tema.BRANCO);
            slogan.setAlignmentX(CENTER_ALIGNMENT);
            slogan.setMaximumSize(slogan.getPreferredSize());
            topo.add(slogan);

            add(topo, BorderLayout.NORTH);
            add(new Ilustracao(carregarImagem()), BorderLayout.CENTER);
            add(criarDestaques(), BorderLayout.SOUTH);
        }

        private static JComponent criarDestaques() {
            JPanel destaques = new JPanel(new GridLayout(1, 3, 0, 0));
            destaques.setOpaque(false);
            destaques.add(destaque(Icone.Tipo.ESCUDO, "Controle total", "do seu estoque", false));
            destaques.add(destaque(Icone.Tipo.GRAFICO, "Vendas", "mais eficientes", true));
            destaques.add(destaque(Icone.Tipo.USUARIOS, "Gestão simples", "e intuitiva", true));
            return destaques;
        }

        private static JComponent destaque(Icone.Tipo tipo, String linha1, String linha2,
                                           boolean separador) {
            JLabel rotulo = new JLabel("<html>" + linha1 + "<br>" + linha2 + "</html>",
                    new Icone(tipo, 26, Tema.AMARELO), JLabel.LEFT);
            rotulo.setIconTextGap(10);
            rotulo.setFont(Tema.fonteNegrito(13));
            rotulo.setForeground(Tema.BRANCO);
            rotulo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, separador ? 1 : 0, 0, 0,
                            Tema.comAlfa(Tema.BRANCO, 90)),
                    BorderFactory.createEmptyBorder(4, separador ? 14 : 0, 4, 4)));
            return rotulo;
        }

        private static Image carregarImagem() {
            try (InputStream entrada = TelaLogin.class.getResourceAsStream(IMAGEM_PRODUTOS)) {
                return entrada == null ? null : ImageIO.read(entrada);
            } catch (IOException erro) {
                return null;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int l = getWidth();
            int a = getHeight();

            // Faixa azul mais clara atravessando o meio.
            Path2D faixa = new Path2D.Double();
            faixa.moveTo(0, a * 0.18);
            faixa.curveTo(l * 0.35, a * 0.05, l * 0.7, a * 0.35, l, a * 0.22);
            faixa.lineTo(l, a * 0.62);
            faixa.curveTo(l * 0.7, a * 0.78, l * 0.3, a * 0.5, 0, a * 0.68);
            faixa.closePath();
            g2.setColor(Tema.comAlfa(Tema.AZUL_CLARO, 110));
            g2.fill(faixa);

            // Onda amarela no canto superior esquerdo.
            Path2D topo = new Path2D.Double();
            topo.moveTo(0, 0);
            topo.lineTo(l * 0.22, 0);
            topo.curveTo(l * 0.12, a * 0.04, l * 0.05, a * 0.08, 0, a * 0.14);
            topo.closePath();
            g2.setColor(Tema.AMARELO);
            g2.fill(topo);

            // Onda amarela no canto inferior direito.
            Path2D base = new Path2D.Double();
            base.moveTo(l, a * 0.8);
            base.curveTo(l * 0.9, a * 0.9, l * 0.75, a * 0.97, l * 0.55, a);
            base.lineTo(l, a);
            base.closePath();
            g2.fill(base);

            g2.dispose();
        }
    }

    // Mostra a imagem do Figma, se existir; senão desenha uma ilustração simples.
    private static class Ilustracao extends JComponent {

        private final Image imagem;

        Ilustracao(Image imagem) {
            this.imagem = imagem;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            if (imagem != null) {
                // Reduz para caber, mas não amplia além do tamanho original (evita borrar).
                double escala = Math.min(1.0, Math.min(
                        getWidth() / (double) imagem.getWidth(null),
                        getHeight() / (double) imagem.getHeight(null)));
                int largura = (int) (imagem.getWidth(null) * escala);
                int altura = (int) (imagem.getHeight(null) * escala);
                g2.drawImage(imagem, (getWidth() - largura) / 2,
                        (getHeight() - altura) / 2, largura, altura, null);
            } else {
                desenharCesta(g2);
            }
            g2.dispose();
        }

        private void desenharCesta(Graphics2D g2) {
            double escala = Math.min(getWidth() / 420.0, getHeight() / 300.0);
            g2.translate((getWidth() - 420 * escala) / 2, (getHeight() - 300 * escala) / 2);
            g2.scale(escala, escala);

            Color branco = new Color(0xF4F8FF);
            Color sombra = new Color(0xC9D6EA);

            // Pilha de pratos atrás da cesta.
            for (int i = 0; i < 4; i++) {
                g2.setColor(i % 2 == 0 ? branco : sombra);
                g2.fillOval(150 - i * 2, 40 + i * 6, 130, 130);
            }

            // Copos empilhados.
            for (int i = 0; i < 3; i++) {
                int x = 70 + i * 34;
                Path2D copo = new Path2D.Double();
                copo.moveTo(x, 50 + i * 12);
                copo.lineTo(x + 34, 50 + i * 12);
                copo.lineTo(x + 28, 150);
                copo.lineTo(x + 6, 150);
                copo.closePath();
                g2.setColor(branco);
                g2.fill(copo);
                g2.setColor(sombra);
                g2.draw(copo);
            }

            // Cesta azul.
            Path2D cesta = new Path2D.Double();
            cesta.moveTo(40, 130);
            cesta.lineTo(300, 130);
            cesta.lineTo(280, 250);
            cesta.lineTo(60, 250);
            cesta.closePath();
            g2.setColor(new Color(0x2563EB));
            g2.fill(cesta);

            g2.setColor(new Color(0x1D4ED8));
            for (int linha = 0; linha < 3; linha++) {
                for (int coluna = 0; coluna < 7; coluna++) {
                    g2.fill(new RoundRectangle2D.Double(
                            70 + coluna * 31 + linha * 2, 148 + linha * 32, 20, 22, 6, 6));
                }
            }

            // Pilha de copos e pratos ao lado da cesta.
            for (int i = 0; i < 5; i++) {
                g2.setColor(i % 2 == 0 ? branco : sombra);
                g2.fillOval(250, 200 - i * 8, 120, 40);
            }
            Path2D pilha = new Path2D.Double();
            pilha.moveTo(330, 60);
            pilha.lineTo(380, 60);
            pilha.lineTo(372, 210);
            pilha.lineTo(338, 210);
            pilha.closePath();
            g2.setColor(branco);
            g2.fill(pilha);
            g2.setColor(sombra);
            for (int y = 75; y < 210; y += 14) {
                g2.drawLine(334, y, 376, y);
            }
        }
    }
}
