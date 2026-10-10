package compartilhado.frontend;

import javax.swing.Icon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

// Ícones de traço desenhados em uma grade de 24x24 e escalados para o tamanho pedido.
public class Icone implements Icon {

    public enum Tipo {
        CUBO, CASA, PRODUTOS, VENDAS, COMPRAS, FINANCEIRO, RELATORIOS,
        USUARIOS, CONFIGURACOES, BUSCA, SINO, CALENDARIO, EMAIL, CADEADO,
        OLHO, OLHO_FECHADO, SETA_DIREITA, SETA_BAIXO, CARTEIRA, ALERTA,
        EXCLAMACAO, GRAFICO, SACOLA, DOCUMENTO, MALETA, ESCUDO, ETIQUETA
    }

    private final Tipo tipo;
    private final int tamanho;
    private final Color cor;
    private final float espessura;

    public Icone(Tipo tipo, int tamanho, Color cor) {
        this(tipo, tamanho, cor, 2f);
    }

    public Icone(Tipo tipo, int tamanho, Color cor, float espessura) {
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.cor = cor;
        this.espessura = espessura;
    }

    @Override
    public int getIconWidth() {
        return tamanho;
    }

    @Override
    public int getIconHeight() {
        return tamanho;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.translate(x, y);
        g2.scale(tamanho / 24.0, tamanho / 24.0);
        g2.setColor(cor);
        g2.setStroke(new BasicStroke(espessura, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        desenhar(g2);
        g2.dispose();
    }

    private void desenhar(Graphics2D g) {
        switch (tipo) {
            case CUBO -> {
                g.draw(poligono(12, 2, 21, 7, 21, 17, 12, 22, 3, 17, 3, 7));
                g.draw(linhas(3, 7, 12, 12, 21, 7));
                g.draw(new Line2D.Double(12, 12, 12, 22));
                g.draw(new Line2D.Double(7.5, 4.5, 16.5, 9.5));
            }
            case CASA -> {
                g.draw(linhas(3, 10, 12, 3, 21, 10));
                g.draw(linhas(5, 9, 5, 21, 19, 21, 19, 9));
                g.draw(linhas(10, 21, 10, 15, 14, 15, 14, 21));
            }
            case PRODUTOS -> {
                g.draw(new RoundRectangle2D.Double(2, 3, 20, 5, 2, 2));
                g.draw(linhas(4, 8, 4, 20, 20, 20, 20, 8));
                g.draw(new Line2D.Double(10, 12, 14, 12));
            }
            case VENDAS -> {
                g.draw(new RoundRectangle2D.Double(5, 4, 14, 18, 3, 3));
                g.draw(new RoundRectangle2D.Double(9, 2, 6, 4, 2, 2));
                g.draw(new Line2D.Double(8.5, 11, 15.5, 11));
                g.draw(new Line2D.Double(8.5, 15, 13.5, 15));
            }
            case COMPRAS -> {
                g.draw(linhas(2, 3, 5, 3, 7.5, 15, 18.5, 15, 21, 7, 6, 7));
                g.draw(new Ellipse2D.Double(7.5, 17.5, 3, 3));
                g.draw(new Ellipse2D.Double(15.5, 17.5, 3, 3));
            }
            case FINANCEIRO -> {
                g.draw(poligono(3, 9, 12, 4, 21, 9));
                for (int coluna : new int[]{6, 10, 14, 18}) {
                    g.draw(new Line2D.Double(coluna, 11.5, coluna, 17.5));
                }
                g.draw(new Line2D.Double(3, 20, 21, 20));
            }
            case RELATORIOS -> {
                g.draw(new RoundRectangle2D.Double(4, 3, 16, 18, 3, 3));
                g.draw(new Line2D.Double(8.5, 17, 8.5, 13));
                g.draw(new Line2D.Double(12, 17, 12, 8));
                g.draw(new Line2D.Double(15.5, 17, 15.5, 11));
            }
            case USUARIOS -> {
                g.draw(new Ellipse2D.Double(5.5, 4, 7, 7));
                g.draw(new Arc2D.Double(2.5, 13, 13, 14, 0, 180, Arc2D.OPEN));
                g.draw(new Ellipse2D.Double(15, 6, 5, 5));
                g.draw(new Arc2D.Double(15.5, 13.5, 7, 10, 0, 140, Arc2D.OPEN));
            }
            case CONFIGURACOES -> {
                g.draw(new Ellipse2D.Double(9, 9, 6, 6));
                g.draw(new Ellipse2D.Double(5, 5, 14, 14));
                for (int i = 0; i < 8; i++) {
                    double angulo = Math.toRadians(i * 45);
                    g.draw(new Line2D.Double(
                            12 + Math.cos(angulo) * 7, 12 + Math.sin(angulo) * 7,
                            12 + Math.cos(angulo) * 9.5, 12 + Math.sin(angulo) * 9.5));
                }
            }
            case BUSCA -> {
                g.draw(new Ellipse2D.Double(4, 4, 13, 13));
                g.draw(new Line2D.Double(15.5, 15.5, 20.5, 20.5));
            }
            case SINO -> {
                g.draw(new Arc2D.Double(6, 4, 12, 12, 0, 180, Arc2D.OPEN));
                g.draw(linhas(6, 10, 6, 15, 4, 18, 20, 18, 18, 15, 18, 10));
                g.draw(new Arc2D.Double(10, 19, 4, 3, 180, 180, Arc2D.OPEN));
            }
            case CALENDARIO -> {
                g.draw(new RoundRectangle2D.Double(3, 5, 18, 16, 3, 3));
                g.draw(new Line2D.Double(3, 10, 21, 10));
                g.draw(new Line2D.Double(8, 3, 8, 7));
                g.draw(new Line2D.Double(16, 3, 16, 7));
                for (int linha = 0; linha < 2; linha++) {
                    for (int coluna = 0; coluna < 3; coluna++) {
                        g.fill(new Ellipse2D.Double(7 + coluna * 4.5, 13 + linha * 4, 1.6, 1.6));
                    }
                }
            }
            case EMAIL -> {
                g.draw(new RoundRectangle2D.Double(2, 5, 20, 14, 3, 3));
                g.draw(linhas(2.5, 6.5, 12, 13, 21.5, 6.5));
            }
            case CADEADO -> {
                g.draw(new RoundRectangle2D.Double(4, 11, 16, 10, 3, 3));
                g.draw(new Arc2D.Double(8, 4, 8, 8, 0, 180, Arc2D.OPEN));
                g.draw(new Line2D.Double(8, 8, 8, 11));
                g.draw(new Line2D.Double(16, 8, 16, 11));
            }
            case OLHO, OLHO_FECHADO -> {
                Path2D olho = new Path2D.Double();
                olho.moveTo(2, 12);
                olho.quadTo(12, 2, 22, 12);
                olho.quadTo(12, 22, 2, 12);
                g.draw(olho);
                g.draw(new Ellipse2D.Double(9, 9, 6, 6));
                if (tipo == Tipo.OLHO_FECHADO) {
                    g.draw(new Line2D.Double(4, 4, 20, 20));
                }
            }
            case SETA_DIREITA -> {
                g.draw(new Line2D.Double(5, 12, 19, 12));
                g.draw(linhas(13, 6, 19, 12, 13, 18));
            }
            case SETA_BAIXO -> g.draw(linhas(6, 9, 12, 15, 18, 9));
            case CARTEIRA -> {
                g.draw(new RoundRectangle2D.Double(3, 8, 18, 13, 4, 4));
                g.draw(linhas(8, 8, 8, 5, 16, 5, 16, 8));
                g.draw(new Ellipse2D.Double(10, 12, 4, 4));
            }
            case ALERTA -> {
                g.draw(poligono(12, 3, 22, 20, 2, 20));
                g.draw(new Line2D.Double(12, 9, 12, 13.5));
                g.fill(new Ellipse2D.Double(11, 16, 2, 2));
            }
            case EXCLAMACAO -> {
                g.draw(new Ellipse2D.Double(3, 3, 18, 18));
                g.draw(new Line2D.Double(12, 7.5, 12, 12.5));
                g.fill(new Ellipse2D.Double(11, 15, 2, 2));
            }
            case GRAFICO -> {
                g.draw(new Line2D.Double(5, 20, 5, 16));
                g.draw(new Line2D.Double(10, 20, 10, 12));
                g.draw(new Line2D.Double(15, 20, 15, 14));
                g.draw(new Line2D.Double(20, 20, 20, 7));
                g.draw(linhas(4, 10, 10, 5, 14, 8, 20, 3));
            }
            case SACOLA -> {
                g.draw(poligono(5, 8, 19, 8, 18, 21, 6, 21));
                g.draw(new Arc2D.Double(9, 3, 6, 7, 0, 180, Arc2D.OPEN));
                g.draw(new Line2D.Double(9, 6.5, 9, 10));
                g.draw(new Line2D.Double(15, 6.5, 15, 10));
            }
            case DOCUMENTO -> {
                g.draw(new RoundRectangle2D.Double(5, 3, 14, 18, 3, 3));
                g.draw(new Line2D.Double(9, 8, 15, 8));
                g.draw(new Line2D.Double(9, 12, 15, 12));
                g.draw(new Line2D.Double(9, 16, 13, 16));
            }
            case MALETA -> {
                g.draw(new RoundRectangle2D.Double(3, 8, 18, 12, 3, 3));
                g.draw(linhas(9, 8, 9, 5, 15, 5, 15, 8));
                g.draw(new Line2D.Double(3, 13, 21, 13));
                g.draw(new Line2D.Double(12, 12, 12, 14.5));
            }
            case ESCUDO -> {
                Path2D escudo = new Path2D.Double();
                escudo.moveTo(12, 2);
                escudo.lineTo(20, 5);
                escudo.lineTo(20, 11);
                escudo.quadTo(20, 18, 12, 22);
                escudo.quadTo(4, 18, 4, 11);
                escudo.lineTo(4, 5);
                escudo.closePath();
                g.draw(escudo);
                g.draw(linhas(8.5, 12, 11, 14.5, 15.5, 9.5));
            }
            case ETIQUETA -> {
                g.draw(poligono(3, 3, 12, 3, 21, 12, 12, 21, 3, 12));
                g.fill(new Ellipse2D.Double(6.5, 6.5, 3, 3));
            }
        }
    }

    private static Path2D linhas(double... pontos) {
        Path2D caminho = new Path2D.Double();
        caminho.moveTo(pontos[0], pontos[1]);
        for (int i = 2; i < pontos.length; i += 2) {
            caminho.lineTo(pontos[i], pontos[i + 1]);
        }
        return caminho;
    }

    private static Path2D poligono(double... pontos) {
        Path2D caminho = linhas(pontos);
        caminho.closePath();
        return caminho;
    }
}
