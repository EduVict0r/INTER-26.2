package compartilhado.frontend;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// Etiqueta em formato de pílula usada nos status ("Ativo", "Sem estoque", "Entrada"...).
public class Badge extends JLabel {

    private Color corFundo = Tema.FUNDO;

    public Badge() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(Tema.fonteNegrito(11));
        setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        setOpaque(false);
    }

    public void configurar(String texto, Color corTexto, Color corFundo) {
        setText(texto);
        setForeground(corTexto);
        this.corFundo = corFundo;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int largura = getFontMetrics(getFont()).stringWidth(getText()) + 20;
        int altura = getFontMetrics(getFont()).getHeight() + 4;
        int x = (getWidth() - largura) / 2;
        int y = (getHeight() - altura) / 2;

        g2.setColor(corFundo);
        g2.fillRoundRect(x, y, largura, altura, altura, altura);
        g2.dispose();
        super.paintComponent(g);
    }
}
