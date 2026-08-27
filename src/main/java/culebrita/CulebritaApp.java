package culebrita;

import culebrita.model.Game;
import culebrita.persist.PreferenceScoreRepository;
import culebrita.ui.GameFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada del programa. Solo arranca la ventana en el hilo de Swing.
 */
public final class CulebritaApp {

    /** Evita que alguien cree instancias: esta clase solo tiene main. */
    private CulebritaApp() {
    }

    public static void main(String[] args) {
        // Swing exige crear ventanas en el Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            try {
                // Aspecto nativo de Windows / del sistema.
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Si falla, se deja el look & feel por defecto.
            }

            // Lógica del juego + récord guardado en el usuario de Windows.
            Game game = new Game(new PreferenceScoreRepository());
            GameFrame frame = new GameFrame(game);
            frame.setVisible(true);
        });
    }
}
