package estoque.backend;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Uma linha do histórico de movimentações (entrada ou saída de estoque).
public class MovimentacaoEstoque {

    private final LocalDateTime data_hora_movimentacao;
    private final String nome_produto;
    private final String tipo_movimentacao;
    private final BigDecimal quantidade;

    public MovimentacaoEstoque(LocalDateTime data_hora_movimentacao, String nome_produto,
                               String tipo_movimentacao, BigDecimal quantidade) {
        this.data_hora_movimentacao = data_hora_movimentacao;
        this.nome_produto = nome_produto;
        this.tipo_movimentacao = tipo_movimentacao;
        this.quantidade = quantidade;
    }

    public LocalDateTime getData_hora_movimentacao() {
        return data_hora_movimentacao;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public String getTipo_movimentacao() {
        return tipo_movimentacao;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public boolean isEntrada() {
        return "ENTRADA".equalsIgnoreCase(tipo_movimentacao);
    }

    public boolean isSaida() {
        return "SAIDA".equalsIgnoreCase(tipo_movimentacao)
                || "SAÍDA".equalsIgnoreCase(tipo_movimentacao);
    }
}
