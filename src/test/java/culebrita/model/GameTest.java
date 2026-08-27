package culebrita.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
    private InMemoryScoreRepository scores;
    private Game game;

    @BeforeEach
    void setUp() {
        scores = new InMemoryScoreRepository();
        game = new Game(12, 8, new Random(1), scores);
    }

    @Test
    void startsOnMenuWithInitialSnake() {
        assertEquals(GamePhase.MENU, game.phase());
        assertEquals(Game.INITIAL_LENGTH, game.snake().length());
    }

    @Test
    void oppositeDirectionIsIgnored() {
        game.start();
        assertEquals(Direction.RIGHT, game.snake().direction());
        game.queueDirection(Direction.LEFT);
        game.tick();
        assertEquals(Direction.RIGHT, game.snake().direction());
        assertEquals(new Cell(7, 4), game.snake().head());
    }

    @Test
    void hittingTheWallEndsTheGame() {
        game.start();
        for (int i = 0; i < 20; i++) {
            game.tick();
        }
        assertEquals(GamePhase.GAME_OVER, game.phase());
    }

    @Test
    void eatingFoodGrowsAndIncreasesScore() {
        game.start();
        Cell ahead = game.snake().head().translated(Direction.RIGHT);
        game.setFood(ahead);
        game.tick();

        assertEquals(1, game.score());
        assertEquals(Game.INITIAL_LENGTH + 1, game.snake().length());
        assertEquals(1, scores.load());
        assertTrue(game.food() == null || !game.snake().occupies(game.food()));
    }

    @Test
    void foodNeverSpawnsOnTheSnake() {
        game.start();
        for (int i = 0; i < 30 && game.phase() == GamePhase.RUNNING; i++) {
            if (game.food() != null) {
                assertFalse(game.snake().occupies(game.food()));
            }
            game.tick();
        }
    }

    private static final class InMemoryScoreRepository implements ScoreRepository {
        private int highScore;

        @Override
        public int load() {
            return highScore;
        }

        @Override
        public void saveIfBest(int score) {
            if (score > highScore) {
                highScore = score;
            }
        }
    }
}
