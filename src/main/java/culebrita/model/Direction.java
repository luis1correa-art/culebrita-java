package culebrita.model;

/**
 * Dirección de movimiento. dx / dy dicen cuánto se mueve en X e Y por un paso
 * (ejemplo: LEFT es -1 en X y 0 en Y).
 */
public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int dx() {
        return dx;
    }

    public int dy() {
        return dy;
    }

    /** True si es el giro de 180° (derecha vs izquierda, arriba vs abajo). Eso se prohíbe en juego. */
    public boolean isOpposite(Direction other) {
        return other != null && dx + other.dx == 0 && dy + other.dy == 0;
    }
}
