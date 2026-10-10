package compartilhado.frontend;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// Barra lateral azul com o logo e o menu de navegação.
public class MenuLateral extends JPanel {

    public static final String DASHBOARD = "Dashboard";
    public static final String PRODUTOS = "Produtos";
    public static final String ESTOQUE = "Estoque";
    public static final String VENDAS = "Vendas";
    public static final String COMPRAS = "Compras";
    public static final String FINANCEIRO = "Financeiro";
    public static final String RELATORIOS = "Relatórios";
    public static final String USUARIOS = "Usuários";
    public static final String CONFIGURACOES = "Configurações";

    private final List<ItemMenu> itens = new ArrayList<>();
    private final Consumer<String> aoSelecionar;

    public MenuLateral(Consumer<String> aoSelecionar) {
        this.aoSelecionar = aoSelecionar;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Tema.AZUL);
        setPreferredSize(new Dimension(220, 0));
        setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));

        Logo logo = new Logo(22, Tema.BRANCO);
        logo.setAlignmentX(LEFT_ALIGNMENT);
        logo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        add(logo);
        add(Box.createVerticalStrut(28));

        adicionar(DASHBOARD, Icone.Tipo.CASA);
        adicionar(PRODUTOS, Icone.Tipo.PRODUTOS);
        adicionar(ESTOQUE, Icone.Tipo.CUBO);
        adicionar(VENDAS, Icone.Tipo.VENDAS);
        adicionar(COMPRAS, Icone.Tipo.COMPRAS);
        adicionar(FINANCEIRO, Icone.Tipo.FINANCEIRO);
        adicionar(RELATORIOS, Icone.Tipo.RELATORIOS);
        adicionar(USUARIOS, Icone.Tipo.USUARIOS);
        adicionar(CONFIGURACOES, Icone.Tipo.CONFIGURACOES);

        add(Box.createVerticalGlue());
    }

    public void setAtivo(String nome) {
        for (ItemMenu item : itens) {
            item.ativo = item.nome.equals(nome);
            item.repaint();
        }
    }

    private void adicionar(String nome, Icone.Tipo tipoIcone) {
        ItemMenu item = new ItemMenu(nome, tipoIcone);
        item.setAlignmentX(LEFT_ALIGNMENT);
        itens.add(item);
        add(item);
        add(Box.createVerticalStrut(4));
    }

    // Onda amarela no canto inferior, como no Figma.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int l = getWidth();
        int a = getHeight();

        Path2D onda = new Path2D.Double();
        onda.moveTo(0, a - 150);
        onda.curveTo(l * 0.35, a - 150, l * 0.55, a - 40, l, a - 70);
        onda.lineTo(l, a);
        onda.lineTo(0, a);
        onda.closePath();
        g2.setColor(Tema.AMARELO);
        g2.fill(onda);

        Path2D faixa = new Path2D.Double();
        faixa.moveTo(0, a - 95);
        faixa.curveTo(l * 0.4, a - 90, l * 0.6, a - 10, l, a - 25);
        faixa.lineTo(l, a);
        faixa.lineTo(0, a);
        faixa.closePath();
        g2.setColor(Tema.AZUL);
        g2.fill(faixa);

        g2.dispose();
    }

    private class ItemMenu extends JComponent {

        private final String nome;
        private final Icone icone;
        private final Icone iconeAtivo;
        private boolean ativo;
        private boolean hover;

        ItemMenu(String nome, Icone.Tipo tipoIcone) {
            this.nome = nome;
            this.icone = new Icone(tipoIcone, 18, Tema.BRANCO);
            this.iconeAtivo = new Icone(tipoIcone, 18, Tema.AZUL);

            setFont(Tema.fonteNegrito(14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(188, 38));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    aoSelecionar.accept(nome);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (ativo) {
                g2.setColor(Tema.AMARELO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            } else if (hover) {
                g2.setColor(Tema.comAlfa(Tema.BRANCO, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }

            int yIcone = (getHeight() - 18) / 2;
            (ativo ? iconeAtivo : icone).paintIcon(this, g2, 12, yIcone);

            g2.setFont(getFont());
            g2.setColor(ativo ? Tema.AZUL : Tema.BRANCO);
            int yTexto = (getHeight() + g2.getFontMetrics().getAscent()
                    - g2.getFontMetrics().getDescent()) / 2;
            g2.drawString(nome, 42, yTexto);
            g2.dispose();
        }
    }
}
