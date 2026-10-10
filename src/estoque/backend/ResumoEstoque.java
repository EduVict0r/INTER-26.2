package estoque.backend;

import java.math.BigDecimal;
import java.util.List;

// Totais calculados a partir da lista de produtos ativos.
public record ResumoEstoque(
        int totalProdutos,
        BigDecimal valorTotal,
        int abaixoDoMinimo,
        int semEstoque
) {

    public static ResumoEstoque de(List<Produto> produtos) {
        BigDecimal valor = BigDecimal.ZERO;
        int abaixo = 0;
        int sem = 0;

        for (Produto produto : produtos) {
            valor = valor.add(produto.getValor_em_estoque());

            switch (produto.getSituacaoEstoque()) {
                case BAIXO -> abaixo++;
                case SEM_ESTOQUE -> sem++;
                case NORMAL -> { }
            }
        }

        return new ResumoEstoque(produtos.size(), valor, abaixo, sem);
    }
}
