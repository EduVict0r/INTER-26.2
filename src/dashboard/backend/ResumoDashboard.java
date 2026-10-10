package dashboard.backend;

import java.math.BigDecimal;
import java.util.List;

// Números do mês mostrados no Dashboard (os de estoque vêm da lista de produtos).
public record ResumoDashboard(
        BigDecimal vendasDoMes,
        BigDecimal comprasDoMes,
        BigDecimal contasAPagar,
        int contasPagarVencidas,
        int contasPagarProximas,
        List<ProdutoVendido> maisVendidos
) {

    public record ProdutoVendido(String nome, BigDecimal quantidade, String siglaUnidade) {
    }
}
