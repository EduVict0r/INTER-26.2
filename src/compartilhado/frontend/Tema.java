package compartilhado.frontend;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

// Cores, fontes e formatos usados em todas as telas (baseados no Figma).
public final class Tema {

    public static final Locale BRASIL = Locale.forLanguageTag("pt-BR");

    // Cores principais
    public static final Color AZUL = new Color(0x0B3D91);
    public static final Color AZUL_CLARO = new Color(0x1E56B8);
    public static final Color AMARELO = new Color(0xFFDD00);
    public static final Color AMARELO_ESCURO = new Color(0xF2C800);

    // Fundo e superfícies
    public static final Color FUNDO = new Color(0xF4F6FA);
    public static final Color BRANCO = Color.WHITE;
    public static final Color BORDA = new Color(0xE3E7EE);
    public static final Color LINHA = new Color(0xEEF1F5);

    // Textos
    public static final Color TEXTO = new Color(0x1F2937);
    public static final Color TEXTO_SUAVE = new Color(0x6B7280);
    public static final Color TEXTO_TITULO = new Color(0x0B2E6E);

    // Status
    public static final Color VERDE = new Color(0x16A34A);
    public static final Color VERDE_FUNDO = new Color(0xDCFCE7);
    public static final Color VERMELHO = new Color(0xDC2626);
    public static final Color VERMELHO_FUNDO = new Color(0xFEE2E2);
    public static final Color LARANJA = new Color(0xF59E0B);
    public static final Color AMARELO_STATUS_TEXTO = new Color(0x92610A);
    public static final Color AMARELO_STATUS_FUNDO = new Color(0xFEF08A);
    public static final Color ROXO = new Color(0x7C3AED);

    private static final String FAMILIA = escolherFamilia();

    private static final NumberFormat MOEDA =
            NumberFormat.getCurrencyInstance(BRASIL);

    private static final DecimalFormat QUANTIDADE =
            new DecimalFormat("#,##0.###", DecimalFormatSymbols.getInstance(BRASIL));

    private static final DateTimeFormatter DATA_EXTENSO =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", BRASIL);

    private Tema() {
    }

    private static String escolherFamilia() {
        String[] instaladas = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();

        for (String preferida : new String[]{"Inter", "Segoe UI", "Arial"}) {
            if (Arrays.asList(instaladas).contains(preferida)) {
                return preferida;
            }
        }
        return Font.SANS_SERIF;
    }

    public static Font fonte(int tamanho) {
        return new Font(FAMILIA, Font.PLAIN, tamanho);
    }

    public static Font fonteNegrito(int tamanho) {
        return new Font(FAMILIA, Font.BOLD, tamanho);
    }

    public static String moeda(BigDecimal valor) {
        // O formato pt-BR usa espaço não separável depois de "R$".
        return MOEDA.format(valor == null ? BigDecimal.ZERO : valor)
                .replace(' ', ' ');
    }

    public static String quantidade(BigDecimal valor) {
        return QUANTIDADE.format(valor == null ? BigDecimal.ZERO : valor);
    }

    public static String inteiro(long valor) {
        return QUANTIDADE.format(valor);
    }

    public static String dataPorExtenso(LocalDate data) {
        return DATA_EXTENSO.format(data);
    }

    // "ADMINISTRADOR" -> "Administrador"
    public static String capitalizar(String texto) {
        if (texto == null || texto.isBlank()) {
            return "";
        }
        String minusculo = texto.trim().toLowerCase(BRASIL);
        return Character.toUpperCase(minusculo.charAt(0)) + minusculo.substring(1);
    }

    // Cor com transparência, usada nos fundos dos ícones.
    public static Color comAlfa(Color cor, int alfa) {
        return new Color(cor.getRed(), cor.getGreen(), cor.getBlue(), alfa);
    }
}
