package compartilhado.frontend;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// Card com ícone colorido, título e valor (ex.: "Valor total do estoque  R$ 125.840,50").
public class CardIndicador extends PainelArredondado {

    private final JLabel valor = new JLabel("—");

    public CardIndicador(Icone.Tipo tipoIcone, Color corIcone, String titulo) {
        super(new BorderLayout(14, 0));
        setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        add(criarIcone(tipoIcone, corIcone), BorderLayout.WEST);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel rotulo = new JLabel(titulo);
        rotulo.setFont(Tema.fonte(12));
        rotulo.setForeground(Tema.TEXTO_SUAVE);

        valor.setFont(Tema.fonteNegrito(19));
        valor.setForeground(Tema.TEXTO_TITULO);

        textos.add(Box.createVerticalGlue());
        textos.add(rotulo);
        textos.add(Box.createVerticalStrut(3));
        textos.add(valor);
        textos.add(Box.createVerticalGlue());

        add(textos, BorderLayout.CENTER);
    }

    public void setValor(String texto) {
        valor.setText(texto);
    }

    public void setDica(String texto) {
        setToolTipText(texto);
        valor.setToolTipText(texto);
    }

    // Círculo com fundo clarinho e o ícone no centro.
    private static JComponent criarIcone(Icone.Tipo tipo, Color cor) {
        Icone icone = new Icone(tipo, 24, cor);

        JComponent circulo = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int lado = Math.min(getWidth(), 46);
                int y = (getHeight() - lado) / 2;
                g2.setColor(Tema.comAlfa(cor, 30));
                g2.fillOval(0, y, lado, lado);
                icone.paintIcon(this, g2, (lado - 24) / 2, y + (lado - 24) / 2);
                g2.dispose();
            }
        };
        circulo.setPreferredSize(new Dimension(46, 46));
        return circulo;
    }
}
