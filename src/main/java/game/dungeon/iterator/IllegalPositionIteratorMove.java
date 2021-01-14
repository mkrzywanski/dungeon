package game.dungeon.iterator;

public class IllegalPositionIteratorMove extends RuntimeException {
    public IllegalPositionIteratorMove(String message) {
        super(message);
    }
}
