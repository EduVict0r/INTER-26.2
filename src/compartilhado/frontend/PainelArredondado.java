package compartilhado.frontend;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

// Painel com cantos arredondados e borda opcional (os "cards" do Figma).
public class PainelArredondado extends JPanel {

    private final int raio;
    private Color corFundo;
    private Color corBorda;

    public PainelArredondado(LayoutManager layout, int raio, Color corFundo, Color corBorda) {
        super(layout);
        this.raio = raio;
        this.corFundo = corFundo;
        this.corBorda = corBorda;
        setOpaque(false);
    }

    public PainelArredondado(LayoutManager layout) {
        this(layout, 12, Tema.BRANCO, Tema.BORDA);
    }

    public void setCorFundo(Color corFundo) {
        this.corFundo = corFundo;
        repaint();
    }

    public void setCorBorda(Color corBorda) {
        this.corBorda = corBorda;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        RoundRectangle2D forma = new RoundRectangle2D.Double(
                0.5, 0.5, getWidth() - 1, getHeight() - 1, raio, raio);

        if (corFundo != null) {
            g2.setColor(corFundo);
            g2.fill(forma);
        }
        if (corBorda != null) {
            g2.setColor(corBorda);
            g2.draw(forma);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
