package culebrita.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class Snake {
    private final Deque<Cell> body = new ArrayDeque<>();
    private Direction direction;
    private Direction pending;

    public Snake(Cell head, Direction direction, int initialLength) {
        this.direction = direction;
        this.pending = direction;
        Cell current = head;
        body.add(current);
        Direction reverse = opposite(direction);
        for (int i = 1; i < initialLength; i++) {
            current = current.translated(reverse);
            body.addLast(current);
        }
    }

    public void queueDirection(Direction next) {
        if (next != null && !direction.isOpposite(next)) {
            pending = next;
        }
    }

    public Cell nextHead() {
        return body.peekFirst().translated(pending);
    }

    public void step(boolean grow) {
        direction = pending;
        body.addFirst(nextHead());
        if (!grow) {
            body.removeLast();
        }
    }

    public boolean occupies(Cell cell) {
        return body.contains(cell);
    }

    public boolean hitsBody(Cell next, boolean growing) {
        int index = 0;
        int last = body.size() - 1;
        for (Cell segment : body) {
            if (!growing && index == last) {
                break;
            }
            if (segment.equals(next)) {
                return true;
            }
            index++;
        }
        return false;
    }

    public Cell head() {
        return body.peekFirst();
    }

    public List<Cell> segments() {
        return new ArrayList<>(body);
    }

    public int length() {
        return body.size();
    }

    public Direction direction() {
        return direction;
    }

    private static Direction opposite(Direction direction) {
        switch (direction) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
            default:
                return Direction.LEFT;
        }
    }
}
