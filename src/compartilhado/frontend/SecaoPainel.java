package compartilhado.frontend;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

// Card grande com título, botão "Ver todos" e um conteúdo (lista ou tabela).
public class SecaoPainel extends PainelArredondado {

    private final BotaoArredondado verTodos = BotaoArredondado.secundario("Ver todos");

    public SecaoPainel(String titulo, Icone icone, JComponent conteudo) {
        super(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 18, 14, 18));

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);

        JLabel rotulo = new JLabel(titulo, icone, JLabel.LEFT);
        rotulo.setIconTextGap(8);
        rotulo.setFont(Tema.fonteNegrito(15));
        rotulo.setForeground(Tema.TEXTO_TITULO);

        cabecalho.add(rotulo, BorderLayout.WEST);
        cabecalho.add(verTodos, BorderLayout.EAST);

        add(cabecalho, BorderLayout.NORTH);
        add(conteudo, BorderLayout.CENTER);
    }

    public BotaoArredondado getVerTodos() {
        return verTodos;
    }
}
