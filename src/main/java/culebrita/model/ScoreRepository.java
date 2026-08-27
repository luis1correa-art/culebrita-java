package culebrita.model;

/**
 * Contrato para guardar el récord. El juego no sabe si es archivo, memoria o Preferences.
 */
public interface ScoreRepository {

    /** Mejor puntaje guardado (0 si nunca se ha jugado). */
    int load();

    /** Si este puntaje supera el récord, lo reemplaza. */
    void saveIfBest(int score);
}
