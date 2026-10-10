package compartilhado.frontend;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

// Campo de texto com ícone à esquerda, texto de exemplo (placeholder) e borda arredondada.
public class CampoComIcone extends PainelArredondado {

    private final JTextComponent campo;

    private CampoComIcone(JTextComponent campo, Icon icone) {
        super(new BorderLayout(10, 0), 8, Tema.BRANCO, Tema.BORDA);
        this.campo = campo;

        setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 8));

        campo.setBorder(BorderFactory.createEmptyBorder(9, 0, 9, 0));
        campo.setOpaque(false);
        campo.setFont(Tema.fonte(13));
        campo.setForeground(Tema.TEXTO);

        if (icone != null) {
            add(new JLabel(icone), BorderLayout.WEST);
        }
        add(campo, BorderLayout.CENTER);

        // Destaca a borda quando o campo está em foco.
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setCorBorda(Tema.AZUL_CLARO);
            }

            @Override
            public void focusLost(FocusEvent e) {
                setCorBorda(Tema.BORDA);
            }
        });
    }

    public static CampoComIcone texto(String placeholder, Icon icone) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                desenharPlaceholder(this, g, placeholder);
            }
        };
        return new CampoComIcone(campo, icone);
    }

    public static CampoComIcone senha(String placeholder, Icon icone) {
        JPasswordField campo = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                desenharPlaceholder(this, g, placeholder);
            }
        };
        return new CampoComIcone(campo, icone);
    }

    public JTextComponent getCampo() {
        return campo;
    }

    // Componente extra no lado direito (ex.: botão de mostrar senha).
    public void setComponenteDireita(JComponent componente) {
        add(componente, BorderLayout.EAST);
    }

    private static void desenharPlaceholder(JTextComponent campo, Graphics g, String placeholder) {
        if (placeholder == null || !campo.getText().isEmpty()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Tema.TEXTO_SUAVE);
        g2.setFont(campo.getFont());
        int y = campo.getInsets().top + g2.getFontMetrics().getAscent();
        g2.drawString(placeholder, campo.getInsets().left, y);
        g2.dispose();
    }
}
