package culebrita.model;

import java.util.Objects;

/**
 * Una casilla del tablero (columna x, fila y). Inmutable: no se modifica después de crearla.
 */
public final class Cell {
    private final int x;
    private final int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    /** Devuelve la casilla vecina en esa dirección (arriba, abajo, izquierda o derecha). */
    public Cell translated(Direction direction) {
        return new Cell(x + direction.dx(), y + direction.dy());
    }

    /** Dos celdas son iguales si tienen la misma x e y (sirve para comida y colisiones). */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cell)) {
            return false;
        }
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
