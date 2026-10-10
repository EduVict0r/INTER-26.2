package estoque.backend;

import compartilhado.backend.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public List<Produto> listarAtivos() throws SQLException {

        String sql = """
                SELECT
                    p.id_produto,
                    p.codigo_produto,
                    p.nome_produto,
                    p.custo_produto,
                    p.estoque_atual,
                    p.estoque_minimo,
                    p.estoque_maximo,
                    u.sigla_unidade
                FROM produto p
                JOIN unidade_medida u ON u.id_unidade = p.id_unidade
                WHERE p.status_produto = 'ATIVO'
                ORDER BY p.codigo_produto, p.nome_produto
                """;

        List<Produto> produtos = new ArrayList<>();

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql);
                ResultSet resultado = comando.executeQuery()
        ) {
            while (resultado.next()) {
                produtos.add(new Produto(
                        resultado.getInt("id_produto"),
                        resultado.getString("codigo_produto"),
                        resultado.getString("nome_produto"),
                        resultado.getBigDecimal("custo_produto"),
                        resultado.getBigDecimal("estoque_atual"),
                        resultado.getBigDecimal("estoque_minimo"),
                        resultado.getBigDecimal("estoque_maximo"),
                        resultado.getString("sigla_unidade")
                ));
            }
        }

        return produtos;
    }
}
