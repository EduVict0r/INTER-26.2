package compartilhado.frontend;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

// Botão desenhado à mão para ter cantos arredondados e cores do Figma.
public class BotaoArredondado extends JButton {

    private final Color corFundo;
    private final Color corFundoHover;
    private final Color corBorda;
    private final int raio;

    public BotaoArredondado(String texto, Color corFundo, Color corFundoHover,
                            Color corTexto, Color corBorda, int raio) {
        super(texto);
        this.corFundo = corFundo;
        this.corFundoHover = corFundoHover;
        this.corBorda = corBorda;
        this.raio = raio;

        setForeground(corTexto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setHorizontalTextPosition(SwingConstants.LEFT);
        setIconTextGap(8);
    }

    // Botão amarelo principal ("Entrar").
    public static BotaoArredondado primario(String texto, Icon icone) {
        BotaoArredondado botao = new BotaoArredondado(
                texto, Tema.AMARELO, Tema.AMARELO_ESCURO, Tema.TEXTO_TITULO, null, 8);
        botao.setFont(Tema.fonteNegrito(14));
        botao.setIcon(icone);
        return botao;
    }

    // Botão pequeno com borda ("Ver todos").
    public static BotaoArredondado secundario(String texto) {
        BotaoArredondado botao = new BotaoArredondado(
                texto, Tema.BRANCO, Tema.FUNDO, Tema.TEXTO_TITULO, Tema.BORDA, 8);
        botao.setFont(Tema.fonteNegrito(11));
        botao.setMargin(new Insets(4, 12, 4, 12));
        return botao;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        RoundRectangle2D forma = new RoundRectangle2D.Double(
                0.5, 0.5, getWidth() - 1, getHeight() - 1, raio, raio);

        boolean destacado = getModel().isRollover() || getModel().isPressed();
        Color fundo = !isEnabled() ? Tema.BORDA : destacado ? corFundoHover : corFundo;

        g2.setColor(fundo);
        g2.fill(forma);

        if (corBorda != null) {
            g2.setColor(corBorda);
            g2.draw(forma);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
