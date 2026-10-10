package compartilhado.frontend;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;

// Logo "Central Descartáveis": cubo amarelo + nome em duas linhas.
public class Logo extends JPanel {

    public Logo(int tamanhoTexto, Color corPrimeiraLinha) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);

        int tamanhoIcone = (int) (tamanhoTexto * 1.9);
        add(new JLabel(new Icone(Icone.Tipo.CUBO, tamanhoIcone, Tema.AMARELO, 2.6f)));
        add(Box.createHorizontalStrut(tamanhoTexto / 2));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel central = new JLabel("Central");
        central.setFont(Tema.fonteNegrito(tamanhoTexto));
        central.setForeground(corPrimeiraLinha);

        JLabel descartaveis = new JLabel("Descartáveis");
        descartaveis.setFont(Tema.fonteNegrito((int) (tamanhoTexto * 0.78)));
        descartaveis.setForeground(Tema.AMARELO);

        textos.add(central);
        textos.add(descartaveis);
        add(textos);
    }
}
