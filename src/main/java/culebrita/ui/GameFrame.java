package culebrita.ui;

import culebrita.model.Difficulty;
import culebrita.model.Direction;
import culebrita.model.Game;
import culebrita.model.GamePhase;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.Timer;

public final class GameFrame extends JFrame {
    private final Game game;
    private final GamePanel panel;
    private final Timer timer;

    public GameFrame(Game game) {
        super("Culebrita");
        this.game = game;
        this.panel = new GamePanel(game);
        this.timer = new Timer(game.tickDelayMs(), this::onTick);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(panel);
        pack();
        setLocationRelativeTo(null);

        Input input = new Input();
        addKeyListener(input);
        panel.addKeyListener(input);
        WindowAdapter focus = new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                panel.requestFocusInWindow();
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                panel.requestFocusInWindow();
            }
        };
        addWindowListener(focus);
        addWindowFocusListener(focus);
        timer.start();
    }

    private void onTick(ActionEvent event) {
        game.tick();
        int delay = game.tickDelayMs();
        if (timer.getDelay() != delay) {
            timer.setDelay(delay);
        }
        panel.repaint();
    }

    private final class Input extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            GamePhase phase = game.phase();
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    if (phase == GamePhase.MENU || phase == GamePhase.GAME_OVER || phase == GamePhase.WON) {
                        dispose();
                    } else {
                        game.backToMenu();
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (phase == GamePhase.MENU) {
                        game.start();
                    } else if (phase == GamePhase.GAME_OVER || phase == GamePhase.WON) {
                        game.backToMenu();
                    }
                    break;
                case KeyEvent.VK_R:
                    if (phase != GamePhase.MENU) {
                        game.restart();
                    }
                    break;
                case KeyEvent.VK_P:
                case KeyEvent.VK_SPACE:
                    game.togglePause();
                    break;
                case KeyEvent.VK_1:
                case KeyEvent.VK_NUMPAD1:
                    game.setDifficulty(Difficulty.EASY);
                    break;
                case KeyEvent.VK_2:
                case KeyEvent.VK_NUMPAD2:
                    game.setDifficulty(Difficulty.NORMAL);
                    break;
                case KeyEvent.VK_3:
                case KeyEvent.VK_NUMPAD3:
                    game.setDifficulty(Difficulty.HARD);
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    if (phase == GamePhase.MENU) {
                        cycleDifficulty(-1);
                    } else {
                        game.queueDirection(Direction.LEFT);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if (phase == GamePhase.MENU) {
                        cycleDifficulty(1);
                    } else {
                        game.queueDirection(Direction.RIGHT);
                    }
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    game.queueDirection(Direction.UP);
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    game.queueDirection(Direction.DOWN);
                    break;
                default:
                    break;
            }
            panel.repaint();
        }
    }

    private void cycleDifficulty(int delta) {
        Difficulty[] options = Difficulty.values();
        int index = (game.difficulty().ordinal() + delta + options.length) % options.length;
        game.setDifficulty(options[index]);
    }
}
