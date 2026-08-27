package culebrita.model;

public interface ScoreRepository {
    int load();

    void saveIfBest(int score);
}
