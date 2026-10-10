package compartilhado.frontend;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

// Deixa as JTables com a aparência do Figma (sem grade, linhas espaçadas, cabeçalho limpo).
public final class Tabelas {

    private Tabelas() {
    }

    public static JScrollPane estilizar(JTable tabela) {
        tabela.setRowHeight(42);
        tabela.setShowVerticalLines(false);
        tabela.setShowHorizontalLines(true);
        tabela.setGridColor(Tema.LINHA);
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.setFont(Tema.fonte(12));
        tabela.setForeground(Tema.TEXTO);
        tabela.setSelectionBackground(new Color(0xEAF1FF));
        tabela.setSelectionForeground(Tema.TEXTO);
        tabela.setFillsViewportHeight(true);
        tabela.setBackground(Tema.BRANCO);
        tabela.setFocusable(false);

        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setReorderingAllowed(false);
        cabecalho.setPreferredSize(new Dimension(0, 36));
        cabecalho.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object valor,
                    boolean selecionado, boolean foco, int linha, int coluna) {
                JLabel rotulo = (JLabel) super.getTableCellRendererComponent(
                        t, valor, false, false, linha, coluna);
                rotulo.setFont(Tema.fonteNegrito(12));
                rotulo.setForeground(Tema.TEXTO_TITULO);
                rotulo.setBackground(Tema.BRANCO);
                rotulo.setHorizontalAlignment(
                        coluna == 0 || coluna == 1 ? SwingConstants.LEFT : SwingConstants.CENTER);
                rotulo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDA),
                        BorderFactory.createEmptyBorder(0, 8, 0, 8)));
                return rotulo;
            }
        });

        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setBorder(BorderFactory.createEmptyBorder());
        rolagem.getViewport().setBackground(Tema.BRANCO);
        rolagem.setOpaque(false);
        return rolagem;
    }

    // Renderer de texto simples com cor, negrito e alinhamento configuráveis.
    public static DefaultTableCellRenderer texto(int alinhamento, Color cor, boolean negrito) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object valor,
                    boolean selecionado, boolean foco, int linha, int coluna) {
                JLabel rotulo = (JLabel) super.getTableCellRendererComponent(
                        t, valor, selecionado, false, linha, coluna);
                rotulo.setForeground(cor);
                rotulo.setFont(negrito ? Tema.fonteNegrito(12) : Tema.fonte(12));
                rotulo.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return rotulo;
            }
        };
        renderer.setHorizontalAlignment(alinhamento);
        return renderer;
    }
}
