package culebrita.ui;

import culebrita.model.Cell;
import culebrita.model.Difficulty;
import culebrita.model.Game;
import culebrita.model.GamePhase;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.JPanel;

public final class GamePanel extends JPanel {
    static final int CELL_SIZE = 22;
    private static final Color BG = new Color(15, 18, 24);
    private static final Color GRID = new Color(32, 38, 48);
    private static final Color HEAD = new Color(110, 231, 140);
    private static final Color BODY = new Color(46, 160, 90);
    private static final Color FOOD = new Color(239, 71, 111);
    private static final Color FOOD_GLOW = new Color(255, 183, 197);
    private static final Color HUD = new Color(232, 237, 245);
    private static final Color MUTED = new Color(156, 168, 184);
    private static final Color OVERLAY = new Color(8, 10, 14, 200);

    private final Game game;

    public GamePanel(Game game) {
        this.game = game;
        setBackground(BG);
        setFocusable(true);
        setPreferredSize(new Dimension(game.columns() * CELL_SIZE, game.rows() * CELL_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2);
        drawSnake(g2);
        drawFood(g2);
        drawHud(g2);
        drawOverlay(g2);

        g2.dispose();
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(GRID);
        for (int x = 0; x <= game.columns(); x++) {
            int px = x * CELL_SIZE;
            g2.drawLine(px, 0, px, game.rows() * CELL_SIZE);
        }
        for (int y = 0; y <= game.rows(); y++) {
            int py = y * CELL_SIZE;
            g2.drawLine(0, py, game.columns() * CELL_SIZE, py);
        }
    }

    private void drawSnake(Graphics2D g2) {
        List<Cell> segments = game.snake().segments();
        for (int i = 0; i < segments.size(); i++) {
            Cell cell = segments.get(i);
            int pad = i == 0 ? 2 : 3;
            g2.setColor(i == 0 ? HEAD : BODY);
            g2.fillRoundRect(
                    cell.x() * CELL_SIZE + pad,
                    cell.y() * CELL_SIZE + pad,
                    CELL_SIZE - pad * 2,
                    CELL_SIZE - pad * 2,
                    8,
                    8);
        }
    }

    private void drawFood(Graphics2D g2) {
        Cell food = game.food();
        if (food == null) {
            return;
        }
        int cx = food.x() * CELL_SIZE + CELL_SIZE / 2;
        int cy = food.y() * CELL_SIZE + CELL_SIZE / 2;
        int r = CELL_SIZE / 2 - 3;
        g2.setColor(FOOD);
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);
        g2.setColor(FOOD_GLOW);
        g2.fillOval(cx - r / 2, cy - r / 2 - 2, r / 2, r / 2);
    }

    private void drawHud(Graphics2D g2) {
        g2.setFont(getFont().deriveFont(Font.BOLD, 14f));
        g2.setColor(HUD);
        String left = "Puntaje: " + game.score() + "    Récord: " + game.highScore();
        String right = game.difficulty().label();
        g2.drawString(left, 12, 18);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(right, getWidth() - fm.stringWidth(right) - 12, 18);
    }

    private void drawOverlay(Graphics2D g2) {
        GamePhase phase = game.phase();
        if (phase == GamePhase.RUNNING) {
            return;
        }

        g2.setColor(OVERLAY);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setStroke(new BasicStroke(1.2f));

        if (phase == GamePhase.MENU) {
            drawMenu(g2);
            return;
        }

        String title;
        String subtitle;
        switch (phase) {
            case PAUSED:
                title = "PAUSA";
                subtitle = "P o Espacio para continuar    ESC menú";
                break;
            case GAME_OVER:
                title = "GAME OVER";
                subtitle = "R reiniciar    ENTER menú    ESC salir";
                break;
            case WON:
                title = "¡TABLERO COMPLETO!";
                subtitle = "R reiniciar    ENTER menú";
                break;
            default:
                title = "";
                subtitle = "";
                break;
        }

        g2.setColor(HEAD);
        g2.setFont(getFont().deriveFont(Font.BOLD, 42f));
        drawCentered(g2, title, getHeight() / 2 - 24);

        g2.setColor(HUD);
        g2.setFont(getFont().deriveFont(Font.PLAIN, 16f));
        drawCentered(g2, subtitle, getHeight() / 2 + 16);

        if (phase == GamePhase.GAME_OVER || phase == GamePhase.WON) {
            g2.setColor(MUTED);
            g2.setFont(getFont().deriveFont(Font.PLAIN, 16f));
            drawCentered(g2, "Puntaje: " + game.score() + "    Récord: " + game.highScore(), getHeight() / 2 + 44);
        }
    }

    private void drawMenu(Graphics2D g2) {
        int centerY = getHeight() / 2 - 70;

        g2.setColor(HEAD);
        g2.setFont(getFont().deriveFont(Font.BOLD, 42f));
        drawCentered(g2, "CULEBRITA", centerY);

        g2.setColor(HUD);
        g2.setFont(getFont().deriveFont(Font.BOLD, 18f));
        drawCentered(g2, "Dificultad", centerY + 42);

        int boxesY = centerY + 58;
        drawDifficultyChoices(g2, boxesY);

        int enterY = boxesY + 56;
        drawCenteredBox(g2, "ENTER", enterY, 160, 40, HEAD, BG);

        g2.setColor(MUTED);
        g2.setFont(getFont().deriveFont(Font.PLAIN, 14f));
        drawCentered(g2, "Elige dificultad y presiona ENTER", enterY + 64);
    }

    private void drawDifficultyChoices(Graphics2D g2, int y) {
        Difficulty[] options = Difficulty.values();
        int boxWidth = 140;
        int boxHeight = 40;
        int gap = 16;
        int total = options.length * boxWidth + (options.length - 1) * gap;
        int x = (getWidth() - total) / 2;

        g2.setFont(getFont().deriveFont(Font.BOLD, 14f));
        for (int i = 0; i < options.length; i++) {
            Difficulty option = options[i];
            boolean selected = option == game.difficulty();
            int bx = x + i * (boxWidth + gap);
            g2.setColor(selected ? HEAD : GRID);
            g2.fillRoundRect(bx, y, boxWidth, boxHeight, 10, 10);
            g2.setColor(selected ? BG : HUD);
            String label = (i + 1) + "  " + option.label();
            FontMetrics fm = g2.getFontMetrics();
            int tx = bx + (boxWidth - fm.stringWidth(label)) / 2;
            int ty = y + (boxHeight + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(label, tx, ty);
        }
    }

    private void drawCenteredBox(Graphics2D g2, String text, int y, int width, int height, Color fill, Color textColor) {
        int x = (getWidth() - width) / 2;
        g2.setColor(fill);
        g2.fillRoundRect(x, y, width, height, 10, 10);
        g2.setColor(textColor);
        g2.setFont(getFont().deriveFont(Font.BOLD, 16f));
        FontMetrics fm = g2.getFontMetrics();
        int tx = x + (width - fm.stringWidth(text)) / 2;
        int ty = y + (height + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(text, tx, ty);
    }

    private void drawCentered(Graphics2D g2, String text, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, y);
    }
}
