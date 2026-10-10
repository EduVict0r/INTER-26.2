package estoque.backend;

import compartilhado.backend.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO {

    // Últimas movimentações, da mais recente para a mais antiga.
    public List<MovimentacaoEstoque> listarRecentes(int limite) throws SQLException {

        String sql = """
                SELECT
                    m.data_hora_movimentacao,
                    p.nome_produto,
                    m.tipo_movimentacao,
                    m.quantidade
                FROM movimentacao_estoque m
                JOIN produto p ON p.id_produto = m.id_produto
                ORDER BY m.data_hora_movimentacao DESC, m.id_movimentacao DESC
                LIMIT ?
                """;

        List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {
            comando.setInt(1, limite);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    movimentacoes.add(new MovimentacaoEstoque(
                            resultado.getTimestamp("data_hora_movimentacao").toLocalDateTime(),
                            resultado.getString("nome_produto"),
                            resultado.getString("tipo_movimentacao"),
                            resultado.getBigDecimal("quantidade")
                    ));
                }
            }
        }

        return movimentacoes;
    }
}
