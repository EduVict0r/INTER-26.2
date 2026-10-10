package compartilhado.frontend;

import usuarios.backend.Funcionario;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

// Barra do topo: busca, sino de notificações e menu do usuário logado.
public class BarraSuperior extends PainelArredondado {

    private final CampoComIcone busca =
            CampoComIcone.texto("Buscar no sistema...", new Icone(Icone.Tipo.BUSCA, 16, Tema.TEXTO_SUAVE));

    private final JPopupMenu menuUsuario = new JPopupMenu();

    public BarraSuperior(Funcionario usuario) {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 18));

        busca.setPreferredSize(new Dimension(280, 38));
        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        esquerda.setOpaque(false);
        esquerda.add(busca);
        add(esquerda, BorderLayout.WEST);

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        direita.setOpaque(false);

        JLabel sino = new JLabel(new Icone(Icone.Tipo.SINO, 20, Tema.TEXTO_TITULO));
        sino.setToolTipText("Notificações");
        direita.add(sino);

        JPanel perfil = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        perfil.setOpaque(false);
        perfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        perfil.add(new Avatar(iniciais(usuario.getNome_funcionario())));

        JLabel nomePerfil = new JLabel(Tema.capitalizar(usuario.getPerfil_funcionario()));
        nomePerfil.setFont(Tema.fonteNegrito(14));
        nomePerfil.setForeground(Tema.TEXTO_TITULO);
        nomePerfil.setToolTipText(usuario.getNome_funcionario());
        perfil.add(nomePerfil);
        perfil.add(new JLabel(new Icone(Icone.Tipo.SETA_BAIXO, 16, Tema.TEXTO_TITULO)));

        perfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menuUsuario.show(perfil, 0, perfil.getHeight() + 6);
            }
        });
        direita.add(perfil);

        add(direita, BorderLayout.EAST);
    }

    public void adicionarOpcaoMenu(String texto, Runnable acao) {
        JMenuItem item = new JMenuItem(texto);
        item.setFont(Tema.fonte(13));
        item.addActionListener(e -> acao.run());
        menuUsuario.add(item);
    }

    // Avisa a cada letra digitada na busca.
    public void aoDigitarBusca(Consumer<String> acao) {
        busca.getCampo().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                acao.accept(busca.getCampo().getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                acao.accept(busca.getCampo().getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                acao.accept(busca.getCampo().getText());
            }
        });
    }

    // Avisa quando o usuário aperta Enter na busca.
    public void aoConfirmarBusca(Runnable acao) {
        busca.getCampo().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    acao.run();
                }
            }
        });
    }

    public void limparBusca() {
        busca.getCampo().setText("");
    }

    private static String iniciais(String nome) {
        if (nome == null || nome.isBlank()) {
            return "?";
        }
        String[] partes = nome.trim().split("\\s+");
        String primeira = partes[0].substring(0, 1);
        String ultima = partes.length > 1 ? partes[partes.length - 1].substring(0, 1) : "";
        return (primeira + ultima).toUpperCase(Tema.BRASIL);
    }

    // Círculo azul com as iniciais do usuário.
    private static class Avatar extends JComponent {

        private final String iniciais;

        Avatar(String iniciais) {
            this.iniciais = iniciais;
            setPreferredSize(new Dimension(34, 34));
            setFont(Tema.fonteNegrito(13));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(Tema.AZUL);
            g2.fillOval(0, 0, getWidth(), getHeight());

            g2.setColor(Tema.BRANCO);
            g2.setFont(getFont());
            int x = (getWidth() - g2.getFontMetrics().stringWidth(iniciais)) / 2;
            int y = (getHeight() + g2.getFontMetrics().getAscent()
                    - g2.getFontMetrics().getDescent()) / 2;
            g2.drawString(iniciais, x, y);
            g2.dispose();
        }
    }
}
