package culebrita.model;

public enum Difficulty {
    EASY("Fácil", 160, 50),
    NORMAL("Normal", 110, 40),
    HARD("Difícil", 70, 30);

    private final String label;
    private final int baseDelayMs;
    private final int minDelayMs;

    Difficulty(String label, int baseDelayMs, int minDelayMs) {
        this.label = label;
        this.baseDelayMs = baseDelayMs;
        this.minDelayMs = minDelayMs;
    }

    public String label() {
        return label;
    }

    public int baseDelayMs() {
        return baseDelayMs;
    }

    public int minDelayMs() {
        return minDelayMs;
    }

    public int delayForScore(int score) {
        int faster = (score / 5) * 8;
        return Math.max(minDelayMs, baseDelayMs - faster);
    }
}
