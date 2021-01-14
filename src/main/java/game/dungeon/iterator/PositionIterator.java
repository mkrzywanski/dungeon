package game.dungeon.iterator;

import game.Directions;
import game.Room;

import java.util.Set;

public interface PositionIterator {
    Set<Directions> possibleMoves();
    void move(Directions directions);
    Room currentRoom();
    PositionIterator copy();
    boolean isValidMove(Directions direction);
}
