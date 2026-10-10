package estoque.backend;

import java.math.BigDecimal;

public class Produto {

    public enum SituacaoEstoque { NORMAL, BAIXO, SEM_ESTOQUE }

    private final int id_produto;
    private final String codigo_produto;
    private final String nome_produto;
    private final BigDecimal custo_produto;
    private final BigDecimal estoque_atual;
    private final BigDecimal estoque_minimo;
    private final BigDecimal estoque_maximo;
    private final String sigla_unidade;

    public Produto(int id_produto, String codigo_produto, String nome_produto,
                   BigDecimal custo_produto, BigDecimal estoque_atual,
                   BigDecimal estoque_minimo, BigDecimal estoque_maximo,
                   String sigla_unidade) {
        this.id_produto = id_produto;
        this.codigo_produto = codigo_produto;
        this.nome_produto = nome_produto;
        this.custo_produto = valorOuZero(custo_produto);
        this.estoque_atual = valorOuZero(estoque_atual);
        this.estoque_minimo = valorOuZero(estoque_minimo);
        this.estoque_maximo = valorOuZero(estoque_maximo);
        this.sigla_unidade = sigla_unidade;
    }

    public int getId_produto() {
        return id_produto;
    }

    public String getCodigo_produto() {
        return codigo_produto;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public BigDecimal getCusto_produto() {
        return custo_produto;
    }

    public BigDecimal getEstoque_atual() {
        return estoque_atual;
    }

    public BigDecimal getEstoque_minimo() {
        return estoque_minimo;
    }

    public BigDecimal getEstoque_maximo() {
        return estoque_maximo;
    }

    public String getSigla_unidade() {
        return sigla_unidade;
    }

    // Valor parado no estoque, pelo preço de custo.
    public BigDecimal getValor_em_estoque() {
        return estoque_atual.multiply(custo_produto);
    }

    public SituacaoEstoque getSituacaoEstoque() {
        if (estoque_atual.signum() <= 0) {
            return SituacaoEstoque.SEM_ESTOQUE;
        }
        if (estoque_atual.compareTo(estoque_minimo) < 0) {
            return SituacaoEstoque.BAIXO;
        }
        return SituacaoEstoque.NORMAL;
    }

    private static BigDecimal valorOuZero(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }
}
