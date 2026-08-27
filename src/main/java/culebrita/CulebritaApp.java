package culebrita;

import culebrita.model.Game;
import culebrita.persist.PreferenceScoreRepository;
import culebrita.ui.GameFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class CulebritaApp {
    private CulebritaApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Keep default look and feel.
            }
            Game game = new Game(new PreferenceScoreRepository());
            GameFrame frame = new GameFrame(game);
            frame.setVisible(true);
        });
    }
}
