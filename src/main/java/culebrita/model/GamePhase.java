package culebrita.model;

/**
 * En qué pantalla está el juego. La UI y el Timer se comportan distinto en cada una.
 */
public enum GamePhase {
    MENU,      // pantalla de inicio (elegir dificultad)
    RUNNING,   // partida en curso
    PAUSED,    // congelado
    GAME_OVER, // chocó
    WON        // llenó el tablero (casi nunca pasa)
}
