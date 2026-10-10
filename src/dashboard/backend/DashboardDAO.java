package dashboard.backend;

import compartilhado.backend.Conexao;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {

    // Quantos dias à frente uma conta conta como "próxima do vencimento".
    private static final int DIAS_ALERTA_VENCIMENTO = 7;

    public ResumoDashboard carregar(LocalDate hoje, int limiteMaisVendidos) throws SQLException {

        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate inicioProximoMes = inicioMes.plusMonths(1);

        try (Connection conexao = Conexao.conectar()) {

            BigDecimal vendas = somarNoPeriodo(conexao, """
                    SELECT COALESCE(SUM(valor_total_venda), 0)
                    FROM venda
                    WHERE status_venda = 'CONCLUIDA'
                      AND data_venda >= ? AND data_venda < ?
                    """, inicioMes, inicioProximoMes);

            BigDecimal compras = somarNoPeriodo(conexao, """
                    SELECT COALESCE(SUM(valor_total_compra), 0)
                    FROM compra
                    WHERE status_compra = 'CONCLUIDA'
                      AND data_compra >= ? AND data_compra < ?
                    """, inicioMes, inicioProximoMes);

            String sqlContas = """
                    SELECT
                        COALESCE(SUM(valor_pagar - COALESCE(valor_pago_pagar, 0)), 0) AS em_aberto,
                        COALESCE(SUM(vencimento_pagar < ?), 0) AS vencidas,
                        COALESCE(SUM(vencimento_pagar >= ? AND vencimento_pagar <= ?), 0) AS proximas
                    FROM conta_pagar
                    WHERE status_pagar NOT IN ('PAGO', 'CANCELADO')
                    """;

            BigDecimal contasAPagar;
            int vencidas;
            int proximas;

            try (PreparedStatement comando = conexao.prepareStatement(sqlContas)) {
                comando.setDate(1, Date.valueOf(hoje));
                comando.setDate(2, Date.valueOf(hoje));
                comando.setDate(3, Date.valueOf(hoje.plusDays(DIAS_ALERTA_VENCIMENTO)));

                try (ResultSet resultado = comando.executeQuery()) {
                    resultado.next();
                    contasAPagar = resultado.getBigDecimal("em_aberto");
                    vencidas = resultado.getInt("vencidas");
                    proximas = resultado.getInt("proximas");
                }
            }

            List<ResumoDashboard.ProdutoVendido> maisVendidos = buscarMaisVendidos(
                    conexao, inicioMes, inicioProximoMes, limiteMaisVendidos);

            return new ResumoDashboard(
                    vendas, compras, contasAPagar, vencidas, proximas, maisVendidos);
        }
    }

    private BigDecimal somarNoPeriodo(Connection conexao, String sql,
                                      LocalDate inicio, LocalDate fim) throws SQLException {

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setDate(1, Date.valueOf(inicio));
            comando.setDate(2, Date.valueOf(fim));

            try (ResultSet resultado = comando.executeQuery()) {
                resultado.next();
                return resultado.getBigDecimal(1);
            }
        }
    }

    private List<ResumoDashboard.ProdutoVendido> buscarMaisVendidos(
            Connection conexao, LocalDate inicio, LocalDate fim, int limite) throws SQLException {

        String sql = """
                SELECT
                    p.nome_produto,
                    u.sigla_unidade,
                    SUM(iv.quantidade_item_venda) AS total
                FROM item_venda iv
                JOIN venda v ON v.id_venda = iv.id_venda
                JOIN produto p ON p.id_produto = iv.id_produto
                JOIN unidade_medida u ON u.id_unidade = p.id_unidade
                WHERE v.status_venda = 'CONCLUIDA'
                  AND iv.status_item_venda = 'ATIVO'
                  AND v.data_venda >= ? AND v.data_venda < ?
                GROUP BY p.id_produto, p.nome_produto, u.sigla_unidade
                ORDER BY total DESC
                LIMIT ?
                """;

        List<ResumoDashboard.ProdutoVendido> produtos = new ArrayList<>();

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setDate(1, Date.valueOf(inicio));
            comando.setDate(2, Date.valueOf(fim));
            comando.setInt(3, limite);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    produtos.add(new ResumoDashboard.ProdutoVendido(
                            resultado.getString("nome_produto"),
                            resultado.getBigDecimal("total"),
                            resultado.getString("sigla_unidade")
                    ));
                }
            }
        }

        return produtos;
    }
}
