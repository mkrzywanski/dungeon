package game.dungeon.pathfinding;

import game.Directions;
import game.Room;

import java.util.List;
import java.util.Set;

//Reconsider if allRooms should be passed to this method
public interface DungeonPathFinder {
    List<Directions> findShortestPath(Set<Room> allRooms, Room from, Room to);
}
