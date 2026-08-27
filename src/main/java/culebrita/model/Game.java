package culebrita.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Reglas del Snake: tablero, comida, puntaje y fases. No dibuja ni lee el teclado.
 */
public final class Game {
    public static final int COLUMNS = 32;
    public static final int ROWS = 24;
    public static final int INITIAL_LENGTH = 3;

    private final int columns;
    private final int rows;
    private final Random random;
    private final ScoreRepository scores;

    private Difficulty difficulty = Difficulty.NORMAL;
    private GamePhase phase = GamePhase.MENU;
    private Snake snake;
    private Cell food;
    private int score;
    private int highScore;

    /** Partida normal: tablero 32x24 y récord persistente. */
    public Game(ScoreRepository scores) {
        this(COLUMNS, ROWS, new Random(), scores);
    }

    /** Constructor para tests: tamaño y Random fijos. */
    public Game(int columns, int rows, Random random, ScoreRepository scores) {
        this.columns = columns;
        this.rows = rows;
        this.random = Objects.requireNonNull(random);
        this.scores = Objects.requireNonNull(scores);
        this.highScore = scores.load();
        resetBoard();
    }

    /** Solo se puede cambiar la dificultad en el menú. */
    public void setDifficulty(Difficulty difficulty) {
        if (phase == GamePhase.MENU) {
            this.difficulty = difficulty;
        }
    }

    public void start() {
        resetBoard();
        phase = GamePhase.RUNNING;
    }

    public void restart() {
        start();
    }

    public void backToMenu() {
        resetBoard();
        phase = GamePhase.MENU;
    }

    public void togglePause() {
        if (phase == GamePhase.RUNNING) {
            phase = GamePhase.PAUSED;
        } else if (phase == GamePhase.PAUSED) {
            phase = GamePhase.RUNNING;
        }
    }

    public void queueDirection(Direction direction) {
        if (phase == GamePhase.RUNNING) {
            snake.queueDirection(direction);
        }
    }

    /**
     * Un paso de juego (lo llama el Timer). Si no está RUNNING, no hace nada.
     */
    public void tick() {
        if (phase != GamePhase.RUNNING) {
            return;
        }

        Cell next = snake.nextHead();
        boolean growing = next.equals(food);
        if (isOutOfBounds(next) || snake.hitsBody(next, growing)) {
            finish(GamePhase.GAME_OVER);
            return;
        }

        snake.step(growing);
        if (growing) {
            score++;
            highScore = Math.max(highScore, score);
            scores.saveIfBest(score);
            // Si no queda ninguna celda libre, el jugador llenó el tablero.
            if (!placeFood()) {
                finish(GamePhase.WON);
            }
        }
    }

    public int tickDelayMs() {
        return difficulty.delayForScore(score);
    }

    public int columns() {
        return columns;
    }

    public int rows() {
        return rows;
    }

    public Difficulty difficulty() {
        return difficulty;
    }

    public GamePhase phase() {
        return phase;
    }

    public Snake snake() {
        return snake;
    }

    public Cell food() {
        return food;
    }

    /** Solo para tests: pone la comida en una casilla concreta. */
    void setFood(Cell food) {
        this.food = food;
    }

    public int score() {
        return score;
    }

    public int highScore() {
        return highScore;
    }

    private void finish(GamePhase endPhase) {
        phase = endPhase;
        scores.saveIfBest(score);
        highScore = Math.max(highScore, scores.load());
    }

    /** Puntaje 0, culebra en el centro mirando a la derecha y comida nueva. */
    private void resetBoard() {
        score = 0;
        int startX = columns / 2;
        int startY = rows / 2;
        snake = new Snake(new Cell(startX, startY), Direction.RIGHT, INITIAL_LENGTH);
        placeFood();
    }

    /**
     * Elige al azar una casilla que no esté ocupada. False = no hay espacio (victoria).
     */
    private boolean placeFood() {
        List<Cell> free = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                Cell cell = new Cell(x, y);
                if (!snake.occupies(cell)) {
                    free.add(cell);
                }
            }
        }
        if (free.isEmpty()) {
            food = null;
            return false;
        }
        food = free.get(random.nextInt(free.size()));
        return true;
    }

    private boolean isOutOfBounds(Cell cell) {
        return cell.x() < 0 || cell.y() < 0 || cell.x() >= columns || cell.y() >= rows;
    }
}
