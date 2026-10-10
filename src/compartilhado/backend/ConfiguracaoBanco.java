package compartilhado.backend;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

// Dados de acesso ao MySQL. Ordem de prioridade:
// 1. o que foi digitado na janela de configuração nesta execução;
// 2. variáveis de ambiente DB_URL, DB_USER e DB_PASSWORD;
// 3. arquivo ~/.central-descartaveis/db.properties (fora do projeto, não vai para o Git);
// 4. valores padrão para um MySQL local.
public record ConfiguracaoBanco(String url, String usuario, String senha) {

    public static final String URL_PADRAO = "jdbc:mysql://localhost:3306/controle_estoque";
    public static final String USUARIO_PADRAO = "root";

    public static final Path ARQUIVO = Path.of(
            System.getProperty("user.home"), ".central-descartaveis", "db.properties");

    private static volatile ConfiguracaoBanco escolhida;

    public static ConfiguracaoBanco atual() {
        if (escolhida != null) {
            return escolhida;
        }

        String urlAmbiente = System.getenv("DB_URL");
        if (urlAmbiente != null && !urlAmbiente.isBlank()) {
            return new ConfiguracaoBanco(
                    urlAmbiente,
                    valorOuPadrao(System.getenv("DB_USER"), USUARIO_PADRAO),
                    valorOuPadrao(System.getenv("DB_PASSWORD"), ""));
        }

        Properties arquivo = lerArquivo();
        return new ConfiguracaoBanco(
                valorOuPadrao(arquivo.getProperty("url"), URL_PADRAO),
                valorOuPadrao(arquivo.getProperty("usuario"), USUARIO_PADRAO),
                valorOuPadrao(arquivo.getProperty("senha"), ""));
    }

    // Passa a usar esta configuração e grava no arquivo para as próximas execuções.
    public static void salvar(ConfiguracaoBanco configuracao) throws IOException {
        Properties propriedades = new Properties();
        propriedades.setProperty("url", configuracao.url());
        propriedades.setProperty("usuario", configuracao.usuario());
        propriedades.setProperty("senha", configuracao.senha());

        Files.createDirectories(ARQUIVO.getParent());
        try (OutputStream saida = Files.newOutputStream(ARQUIVO)) {
            propriedades.store(saida, "Acesso ao banco - Central Descartaveis");
        }

        escolhida = configuracao;
    }

    private static Properties lerArquivo() {
        Properties propriedades = new Properties();
        if (Files.exists(ARQUIVO)) {
            try (InputStream entrada = Files.newInputStream(ARQUIVO)) {
                propriedades.load(entrada);
            } catch (IOException erro) {
                System.out.println("Não foi possível ler " + ARQUIVO + ": " + erro.getMessage());
            }
        }
        return propriedades;
    }

    private static String valorOuPadrao(String valor, String padrao) {
        return valor == null || valor.isBlank() ? padrao : valor;
    }
}
