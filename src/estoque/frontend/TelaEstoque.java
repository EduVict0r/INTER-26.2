package estoque.frontend;

import compartilhado.frontend.Badge;
import compartilhado.frontend.CardIndicador;
import compartilhado.frontend.Icone;
import compartilhado.frontend.SecaoPainel;
import compartilhado.frontend.Tabelas;
import compartilhado.frontend.Tema;
import estoque.backend.MovimentacaoDAO;
import estoque.backend.MovimentacaoEstoque;
import estoque.backend.Produto;
import estoque.backend.ProdutoDAO;
import estoque.backend.ResumoEstoque;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

// Página "Controle de estoque": indicadores, tabela de produtos e histórico de movimentações.
public class TelaEstoque extends JPanel {

    private static final int LIMITE_HISTORICO = 50;
    private static final int LIMITE_HISTORICO_COMPLETO = 1000;

    private static final DateTimeFormatter DATA_CURTA = DateTimeFormatter.ofPattern("dd/MM/yy");

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

    private final CardIndicador totalItens =
            new CardIndicador(Icone.Tipo.CUBO, Tema.AZUL_CLARO, "Total de itens");
    private final CardIndicador valorEstoque =
            new CardIndicador(Icone.Tipo.CARTEIRA, Tema.VERDE, "Estoque atual");
    private final CardIndicador abaixoDoMinimo =
            new CardIndicador(Icone.Tipo.EXCLAMACAO, Tema.LARANJA, "Abaixo do mínimo");
    private final CardIndicador semEstoque =
            new CardIndicador(Icone.Tipo.ALERTA, Tema.VERMELHO, "Sem estoque");

    private final DefaultTableModel modeloProdutos = criarModelo(
            "Código", "Produto", "Estoque atual", "Mínimo", "Máximo", "Status");
    private final DefaultTableModel modeloHistorico = criarModelo(
            "Data", "Produto", "Tipo", "Qtd.");

    private final JTable tabelaProdutos = new JTable(modeloProdutos);
    private final JTable tabelaHistorico = new JTable(modeloHistorico);
    private final TableRowSorter<DefaultTableModel> filtroProdutos =
            new TableRowSorter<>(modeloProdutos);

    private final JLabel rodapeProdutos = new JLabel(" ");
    private final SecaoPainel secaoProdutos;
    private final SecaoPainel secaoHistorico;

    private int limiteHistorico = LIMITE_HISTORICO;

    public TelaEstoque() {
        super(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 2;
        c.gridx = 0;
        c.weightx = 1;

        JLabel titulo = new JLabel("Controle de estoque");
        titulo.setFont(Tema.fonteNegrito(26));
        titulo.setForeground(Tema.TEXTO_TITULO);
        c.gridy = 0;
        c.insets = new Insets(0, 0, 20, 0);
        add(titulo, c);

        c.gridy = 1;
        add(criarCards(), c);

        secaoProdutos = new SecaoPainel("Estoque atual", null, criarAreaProdutos());
        secaoHistorico = new SecaoPainel("Histórico de movimentações", null, criarAreaHistorico());

        secaoHistorico.getVerTodos().addActionListener(e -> {
            limiteHistorico = LIMITE_HISTORICO_COMPLETO;
            atualizar();
        });

        // Tamanhos iguais para o GridBag dividir a largura só pelos pesos (62% / 38%).
        secaoProdutos.setPreferredSize(new Dimension(100, 100));
        secaoHistorico.setPreferredSize(new Dimension(100, 100));

        c.gridy = 2;
        c.gridwidth = 1;
        c.weighty = 1;
        c.weightx = 0.62;
        c.insets = new Insets(0, 0, 0, 18);
        add(secaoProdutos, c);

        c.gridx = 1;
        c.weightx = 0.38;
        c.insets = new Insets(0, 0, 0, 0);
        add(secaoHistorico, c);
    }

    // "Ver todos" do estoque atual: quem usa a tela decide o que fazer (ex.: limpar a busca).
    public void aoClicarVerTodosProdutos(Runnable acao) {
        secaoProdutos.getVerTodos().addActionListener(e -> acao.run());
    }

    // Filtra a tabela por código ou nome do produto.
    public void filtrar(String texto) {
        if (texto == null || texto.isBlank()) {
            filtroProdutos.setRowFilter(null);
        } else {
            filtroProdutos.setRowFilter(
                    RowFilter.regexFilter("(?i)" + Pattern.quote(texto.trim()), 0, 1));
        }
        atualizarRodape();
    }

    public void atualizar() {
        int limite = limiteHistorico;

        new SwingWorker<Object[], Void>() {
            @Override
            protected Object[] doInBackground() throws Exception {
                List<Produto> produtos = produtoDAO.listarAtivos();
                List<MovimentacaoEstoque> historico = movimentacaoDAO.listarRecentes(limite);
                return new Object[]{produtos, historico};
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void done() {
                try {
                    Object[] dados = get();
                    preencher((List<Produto>) dados[0], (List<MovimentacaoEstoque>) dados[1]);
                } catch (InterruptedException erro) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException erro) {
                    erro.getCause().printStackTrace();
                    JOptionPane.showMessageDialog(TelaEstoque.this,
                            "Não foi possível carregar o estoque.\n" + erro.getCause().getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    void preencher(List<Produto> produtos, List<MovimentacaoEstoque> historico) {
        ResumoEstoque resumo = ResumoEstoque.de(produtos);
        totalItens.setValor(Tema.inteiro(resumo.totalProdutos()));
        valorEstoque.setValor(Tema.moeda(resumo.valorTotal()));
        abaixoDoMinimo.setValor(Tema.inteiro(resumo.abaixoDoMinimo()));
        semEstoque.setValor(Tema.inteiro(resumo.semEstoque()));

        modeloProdutos.setRowCount(0);
        for (Produto produto : produtos) {
            modeloProdutos.addRow(new Object[]{
                    produto.getCodigo_produto(),
                    produto.getNome_produto(),
                    produto.getEstoque_atual(),
                    produto.getEstoque_minimo(),
                    produto.getEstoque_maximo(),
                    produto.getSituacaoEstoque()
            });
        }

        modeloHistorico.setRowCount(0);
        for (MovimentacaoEstoque movimentacao : historico) {
            modeloHistorico.addRow(new Object[]{
                    DATA_CURTA.format(movimentacao.getData_hora_movimentacao()),
                    movimentacao.getNome_produto(),
                    movimentacao,
                    movimentacao
            });
        }

        atualizarRodape();
    }

    private void atualizarRodape() {
        int visiveis = tabelaProdutos.getRowCount();
        int total = modeloProdutos.getRowCount();
        rodapeProdutos.setText(visiveis == total
                ? "Mostrando " + Tema.inteiro(total) + " registros"
                : "Mostrando " + Tema.inteiro(visiveis) + " de " + Tema.inteiro(total) + " registros");
    }

    private JComponent criarCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 18, 0));
        cards.setOpaque(false);
        cards.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        cards.add(totalItens);
        cards.add(valorEstoque);
        cards.add(abaixoDoMinimo);
        cards.add(semEstoque);
        return cards;
    }

    private JComponent criarAreaProdutos() {
        tabelaProdutos.setRowSorter(filtroProdutos);

        DefaultTableCellRenderer numero =
                Tabelas.texto(SwingConstants.CENTER, Tema.TEXTO, false);

        tabelaProdutos.getColumnModel().getColumn(0)
                .setCellRenderer(Tabelas.texto(SwingConstants.LEFT, Tema.AZUL_CLARO, true));
        tabelaProdutos.getColumnModel().getColumn(1)
                .setCellRenderer(Tabelas.texto(SwingConstants.LEFT, Tema.TEXTO_TITULO, false));
        tabelaProdutos.getColumnModel().getColumn(2).setCellRenderer(new RendererEstoqueAtual());
        tabelaProdutos.getColumnModel().getColumn(3).setCellRenderer(new RendererQuantidade(numero));
        tabelaProdutos.getColumnModel().getColumn(4).setCellRenderer(new RendererQuantidade(numero));
        tabelaProdutos.getColumnModel().getColumn(5).setCellRenderer(new RendererSituacao());

        int[] larguras = {70, 220, 100, 80, 80, 120};
        for (int i = 0; i < larguras.length; i++) {
            tabelaProdutos.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }

        rodapeProdutos.setFont(Tema.fonte(12));
        rodapeProdutos.setForeground(Tema.TEXTO_SUAVE);
        rodapeProdutos.setBorder(BorderFactory.createEmptyBorder(10, 4, 0, 0));

        JPanel area = new JPanel(new BorderLayout());
        area.setOpaque(false);
        area.add(Tabelas.estilizar(tabelaProdutos), BorderLayout.CENTER);
        area.add(rodapeProdutos, BorderLayout.SOUTH);
        return area;
    }

    private JComponent criarAreaHistorico() {
        tabelaHistorico.getColumnModel().getColumn(0)
                .setCellRenderer(Tabelas.texto(SwingConstants.LEFT, Tema.TEXTO, false));
        tabelaHistorico.getColumnModel().getColumn(1)
                .setCellRenderer(Tabelas.texto(SwingConstants.LEFT, Tema.TEXTO, false));
        tabelaHistorico.getColumnModel().getColumn(2).setCellRenderer(new RendererTipo());
        tabelaHistorico.getColumnModel().getColumn(3).setCellRenderer(new RendererQuantidadeMovimentada());

        int[] larguras = {70, 140, 80, 60};
        for (int i = 0; i < larguras.length; i++) {
            tabelaHistorico.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }

        return Tabelas.estilizar(tabelaHistorico);
    }

    private static DefaultTableModel criarModelo(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }

            // Permite ordenar números como números ao clicar no cabeçalho.
            @Override
            public Class<?> getColumnClass(int coluna) {
                return getRowCount() > 0 && getValueAt(0, coluna) != null
                        ? getValueAt(0, coluna).getClass()
                        : Object.class;
            }
        };
    }

    // Quantidade formatada (1.250), usando o renderer base para cor e alinhamento.
    private static class RendererQuantidade implements TableCellRenderer {

        private final DefaultTableCellRenderer base;

        RendererQuantidade(DefaultTableCellRenderer base) {
            this.base = base;
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor,
                boolean selecionado, boolean foco, int linha, int coluna) {
            return base.getTableCellRendererComponent(
                    t, Tema.quantidade((BigDecimal) valor), selecionado, foco, linha, coluna);
        }
    }

    // Estoque atual fica vermelho quando está zerado.
    private static class RendererEstoqueAtual implements TableCellRenderer {

        private final DefaultTableCellRenderer normal =
                Tabelas.texto(SwingConstants.CENTER, Tema.TEXTO, false);
        private final DefaultTableCellRenderer zerado =
                Tabelas.texto(SwingConstants.CENTER, Tema.VERMELHO, true);

        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor,
                boolean selecionado, boolean foco, int linha, int coluna) {
            BigDecimal quantidade = (BigDecimal) valor;
            DefaultTableCellRenderer renderer = quantidade.signum() <= 0 ? zerado : normal;
            return renderer.getTableCellRendererComponent(
                    t, Tema.quantidade(quantidade), selecionado, foco, linha, coluna);
        }
    }

    private static class RendererSituacao implements TableCellRenderer {

        private final Badge badge = new Badge();

        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor,
                boolean selecionado, boolean foco, int linha, int coluna) {
            switch ((Produto.SituacaoEstoque) valor) {
                case SEM_ESTOQUE -> badge.configurar("Sem estoque", Tema.VERMELHO, Tema.VERMELHO_FUNDO);
                case BAIXO -> badge.configurar("Estoque baixo",
                        Tema.AMARELO_STATUS_TEXTO, Tema.AMARELO_STATUS_FUNDO);
                case NORMAL -> badge.configurar("Ativo", Tema.VERDE, Tema.VERDE_FUNDO);
            }
            return badge;
        }
    }

    private static class RendererTipo implements TableCellRenderer {

        private final Badge badge = new Badge();

        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor,
                boolean selecionado, boolean foco, int linha, int coluna) {
            MovimentacaoEstoque movimentacao = (MovimentacaoEstoque) valor;

            if (movimentacao.isEntrada()) {
                badge.configurar("Entrada", Tema.VERDE, Tema.VERDE_FUNDO);
            } else if (movimentacao.isSaida()) {
                badge.configurar("Saída", Tema.VERMELHO, Tema.VERMELHO_FUNDO);
            } else {
                badge.configurar(Tema.capitalizar(movimentacao.getTipo_movimentacao()),
                        Tema.TEXTO_SUAVE, Tema.FUNDO);
            }
            return badge;
        }
    }

    // "+500" para entradas e "-120" para saídas.
    private static class RendererQuantidadeMovimentada implements TableCellRenderer {

        private final DefaultTableCellRenderer base =
                Tabelas.texto(SwingConstants.CENTER, Tema.TEXTO, false);

        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor,
                boolean selecionado, boolean foco, int linha, int coluna) {
            MovimentacaoEstoque movimentacao = (MovimentacaoEstoque) valor;
            String sinal = movimentacao.isEntrada() ? "+" : movimentacao.isSaida() ? "-" : "";
            return base.getTableCellRendererComponent(t,
                    sinal + Tema.quantidade(movimentacao.getQuantidade()),
                    selecionado, foco, linha, coluna);
        }
    }
}
