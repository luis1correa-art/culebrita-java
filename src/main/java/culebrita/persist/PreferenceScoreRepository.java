package culebrita.persist;

import culebrita.model.ScoreRepository;
import java.util.prefs.Preferences;

public final class PreferenceScoreRepository implements ScoreRepository {
    private static final String KEY = "highScore";
    private final Preferences preferences;

    public PreferenceScoreRepository() {
        this(Preferences.userNodeForPackage(PreferenceScoreRepository.class));
    }

    PreferenceScoreRepository(Preferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public int load() {
        return preferences.getInt(KEY, 0);
    }

    @Override
    public void saveIfBest(int score) {
        if (score > load()) {
            preferences.putInt(KEY, score);
        }
    }
}
