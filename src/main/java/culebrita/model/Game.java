package culebrita.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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

    public Game(ScoreRepository scores) {
        this(COLUMNS, ROWS, new Random(), scores);
    }

    public Game(int columns, int rows, Random random, ScoreRepository scores) {
        this.columns = columns;
        this.rows = rows;
        this.random = Objects.requireNonNull(random);
        this.scores = Objects.requireNonNull(scores);
        this.highScore = scores.load();
        resetBoard();
    }

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

    private void resetBoard() {
        score = 0;
        int startX = columns / 2;
        int startY = rows / 2;
        snake = new Snake(new Cell(startX, startY), Direction.RIGHT, INITIAL_LENGTH);
        placeFood();
    }

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
