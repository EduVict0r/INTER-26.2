package dashboard.frontend;

import compartilhado.frontend.BotaoArredondado;
import compartilhado.frontend.CardIndicador;
import compartilhado.frontend.Icone;
import compartilhado.frontend.SecaoPainel;
import compartilhado.frontend.Tema;
import dashboard.backend.DashboardDAO;
import dashboard.backend.ResumoDashboard;
import estoque.backend.Produto;
import estoque.backend.ProdutoDAO;
import estoque.backend.ResumoEstoque;
import usuarios.backend.Funcionario;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

// Página inicial depois do login: indicadores, alertas e produtos mais vendidos.
public class TelaDashboard extends JPanel {

    private static final int LIMITE_MAIS_VENDIDOS = 4;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final DashboardDAO dashboardDAO = new DashboardDAO();

    private final JLabel data = new JLabel();

    private final CardIndicador valorEstoque =
            new CardIndicador(Icone.Tipo.CARTEIRA, Tema.AZUL_CLARO, "Valor total do estoque");
    private final CardIndicador produtosCadastrados =
            new CardIndicador(Icone.Tipo.ETIQUETA, Tema.AMARELO_ESCURO, "Produtos cadastrados");
    private final CardIndicador abaixoDoMinimo =
            new CardIndicador(Icone.Tipo.ALERTA, Tema.VERMELHO, "Produtos abaixo do mínimo");
    private final CardIndicador semEstoque =
            new CardIndicador(Icone.Tipo.EXCLAMACAO, Tema.LARANJA, "Sem estoque");
    private final CardIndicador vendasDoMes =
            new CardIndicador(Icone.Tipo.GRAFICO, Tema.VERDE, "Vendas do mês");
    private final CardIndicador comprasDoMes =
            new CardIndicador(Icone.Tipo.SACOLA, new Color(0x0EA5E9), "Compras do mês");
    private final CardIndicador contasAReceber =
            new CardIndicador(Icone.Tipo.DOCUMENTO, Tema.ROXO, "Contas a receber");
    private final CardIndicador contasAPagar =
            new CardIndicador(Icone.Tipo.MALETA, Tema.AMARELO_ESCURO, "Contas a pagar");

    private final JPanel listaAlertas = criarLista();
    private final JPanel listaMaisVendidos = criarLista();

    private final SecaoPainel secaoAlertas;
    private final SecaoPainel secaoMaisVendidos;

    public TelaDashboard(Funcionario usuario) {
        super(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;

        c.gridy = 0;
        c.insets = new Insets(0, 0, 20, 0);
        add(criarCabecalho(usuario), c);

        c.gridy = 1;
        add(criarCards(), c);

        // Ainda não existe tabela de contas a receber no banco.
        contasAReceber.setValor("—");
        contasAReceber.setDica("O banco ainda não tem uma tabela de contas a receber.");

        secaoAlertas = new SecaoPainel(
                "Alertas e notificações",
                new Icone(Icone.Tipo.SINO, 18, Tema.TEXTO_TITULO),
                envolverNoTopo(listaAlertas));

        secaoMaisVendidos = new SecaoPainel(
                "Produtos mais vendidos (mês)", null, envolverNoTopo(listaMaisVendidos));

        JPanel inferiores = new JPanel(new GridLayout(1, 2, 18, 0));
        inferiores.setOpaque(false);
        inferiores.add(secaoAlertas);
        inferiores.add(secaoMaisVendidos);

        c.gridy = 2;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 0);
        add(inferiores, c);
    }

    public BotaoArredondado getVerTodosAlertas() {
        return secaoAlertas.getVerTodos();
    }

    public BotaoArredondado getVerTodosMaisVendidos() {
        return secaoMaisVendidos.getVerTodos();
    }

    // Busca os dados no banco sem travar a janela.
    public void atualizar() {
        LocalDate hoje = LocalDate.now();
        data.setText(Tema.dataPorExtenso(hoje));

        new SwingWorker<Object[], Void>() {
            @Override
            protected Object[] doInBackground() throws Exception {
                List<Produto> produtos = produtoDAO.listarAtivos();
                ResumoDashboard resumo = dashboardDAO.carregar(hoje, LIMITE_MAIS_VENDIDOS);
                return new Object[]{produtos, resumo};
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void done() {
                try {
                    Object[] dados = get();
                    preencher((List<Produto>) dados[0], (ResumoDashboard) dados[1]);
                } catch (InterruptedException erro) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException erro) {
                    erro.getCause().printStackTrace();
                    JOptionPane.showMessageDialog(TelaDashboard.this,
                            "Não foi possível carregar o dashboard.\n" + erro.getCause().getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    void preencher(List<Produto> produtos, ResumoDashboard resumo) {
        ResumoEstoque estoque = ResumoEstoque.de(produtos);

        valorEstoque.setValor(Tema.moeda(estoque.valorTotal()));
        produtosCadastrados.setValor(Tema.inteiro(estoque.totalProdutos()));
        abaixoDoMinimo.setValor(Tema.inteiro(estoque.abaixoDoMinimo()));
        semEstoque.setValor(Tema.inteiro(estoque.semEstoque()));
        vendasDoMes.setValor(Tema.moeda(resumo.vendasDoMes()));
        comprasDoMes.setValor(Tema.moeda(resumo.comprasDoMes()));
        contasAPagar.setValor(Tema.moeda(resumo.contasAPagar()));

        preencherAlertas(estoque, resumo);
        preencherMaisVendidos(resumo.maisVendidos());
    }

    private void preencherAlertas(ResumoEstoque estoque, ResumoDashboard resumo) {
        listaAlertas.removeAll();

        if (estoque.abaixoDoMinimo() > 0) {
            listaAlertas.add(linhaAlerta(Tema.VERMELHO, estoque.abaixoDoMinimo()
                    + plural(estoque.abaixoDoMinimo(), " produto está", " produtos estão")
                    + " com estoque abaixo do mínimo"));
        }
        if (estoque.semEstoque() > 0) {
            listaAlertas.add(linhaAlerta(Tema.VERMELHO, estoque.semEstoque()
                    + plural(estoque.semEstoque(), " produto", " produtos") + " sem estoque"));
        }
        if (resumo.contasPagarVencidas() > 0) {
            listaAlertas.add(linhaAlerta(Tema.VERMELHO, resumo.contasPagarVencidas()
                    + plural(resumo.contasPagarVencidas(),
                    " conta a pagar está vencida", " contas a pagar estão vencidas")));
        }
        if (resumo.contasPagarProximas() > 0) {
            listaAlertas.add(linhaAlerta(Tema.AMARELO_ESCURO, resumo.contasPagarProximas()
                    + plural(resumo.contasPagarProximas(),
                    " conta a pagar está próxima", " contas a pagar estão próximas")
                    + " do vencimento"));
        }
        if (listaAlertas.getComponentCount() == 0) {
            listaAlertas.add(linhaAlerta(Tema.VERDE, "Nenhum alerta no momento"));
        }

        listaAlertas.revalidate();
        listaAlertas.repaint();
    }

    private void preencherMaisVendidos(List<ResumoDashboard.ProdutoVendido> produtos) {
        listaMaisVendidos.removeAll();

        if (produtos.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma venda registrada neste mês.");
            vazio.setFont(Tema.fonte(13));
            vazio.setForeground(Tema.TEXTO_SUAVE);
            vazio.setBorder(BorderFactory.createEmptyBorder(12, 4, 12, 4));
            listaMaisVendidos.add(vazio);
        }

        int posicao = 1;
        for (ResumoDashboard.ProdutoVendido produto : produtos) {
            JPanel linha = criarLinha();

            JLabel nome = new JLabel(posicao + "     " + produto.nome());
            nome.setFont(Tema.fonteNegrito(13));
            nome.setForeground(Tema.TEXTO_TITULO);

            JLabel quantidade = new JLabel(Tema.quantidade(produto.quantidade()) + " "
                    + produto.siglaUnidade().toLowerCase(Tema.BRASIL));
            quantidade.setFont(Tema.fonte(12));
            quantidade.setForeground(Tema.TEXTO_SUAVE);

            linha.add(nome, BorderLayout.CENTER);
            linha.add(quantidade, BorderLayout.EAST);
            listaMaisVendidos.add(linha);
            posicao++;
        }

        listaMaisVendidos.revalidate();
        listaMaisVendidos.repaint();
    }

    private JComponent criarCabecalho(Funcionario usuario) {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel ola = new JLabel("Olá, " + primeiroNome(usuario.getNome_funcionario()) + "!");
        ola.setFont(Tema.fonteNegrito(26));
        ola.setForeground(Tema.TEXTO_TITULO);

        JLabel subtitulo = new JLabel("Aqui está o resumo das suas operações de hoje.");
        subtitulo.setFont(Tema.fonte(13));
        subtitulo.setForeground(Tema.TEXTO_SUAVE);

        textos.add(ola);
        textos.add(Box.createVerticalStrut(2));
        textos.add(subtitulo);

        data.setFont(Tema.fonte(13));
        data.setForeground(Tema.TEXTO_SUAVE);
        data.setIcon(new Icone(Icone.Tipo.CALENDARIO, 20, Tema.TEXTO_TITULO));
        data.setHorizontalTextPosition(JLabel.LEFT);
        data.setIconTextGap(10);
        data.setText(Tema.dataPorExtenso(LocalDate.now()));

        cabecalho.add(textos, BorderLayout.WEST);
        cabecalho.add(data, BorderLayout.EAST);
        return cabecalho;
    }

    private JComponent criarCards() {
        JPanel cards = new JPanel(new GridLayout(2, 4, 18, 18));
        cards.setOpaque(false);
        cards.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        for (CardIndicador card : new CardIndicador[]{
                valorEstoque, produtosCadastrados, abaixoDoMinimo, semEstoque,
                vendasDoMes, comprasDoMes, contasAReceber, contasAPagar}) {
            card.setPreferredSize(new Dimension(200, 88));
            cards.add(card);
        }
        return cards;
    }

    private static JPanel criarLista() {
        JPanel lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        return lista;
    }

    // Mantém as linhas no topo em vez de esticá-las pela altura toda.
    private static JComponent envolverNoTopo(JComponent conteudo) {
        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setOpaque(false);
        envoltorio.add(conteudo, BorderLayout.NORTH);
        return envoltorio;
    }

    private static JPanel criarLinha() {
        JPanel linha = new JPanel(new BorderLayout(10, 0));
        linha.setOpaque(false);
        linha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.LINHA),
                BorderFactory.createEmptyBorder(12, 4, 12, 4)));
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        linha.setAlignmentX(LEFT_ALIGNMENT);
        return linha;
    }

    private static JPanel linhaAlerta(Color cor, String texto) {
        JPanel linha = criarLinha();

        JComponent bolinha = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cor);
                g2.fillOval(0, (getHeight() - 10) / 2, 10, 10);
                g2.dispose();
            }
        };
        bolinha.setPreferredSize(new Dimension(10, 10));

        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(Tema.fonte(13));
        rotulo.setForeground(Tema.TEXTO);

        linha.add(bolinha, BorderLayout.WEST);
        linha.add(rotulo, BorderLayout.CENTER);
        return linha;
    }

    private static String plural(int quantidade, String singular, String plural) {
        return quantidade == 1 ? singular : plural;
    }

    private static String primeiroNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return "";
        }
        return nome.trim().split("\\s+")[0];
    }
}
