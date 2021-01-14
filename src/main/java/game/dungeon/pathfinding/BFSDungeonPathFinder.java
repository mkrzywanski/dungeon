package game.dungeon.pathfinding;

import game.Directions;
import game.Room;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BFSDungeonPathFinder implements DungeonPathFinder {

    @Override
    public List<Directions> findShortestPath(Set<Room> allRooms, Room from, Room to) {

        Queue<List<Room>> queue = new ArrayDeque<>();
        Set<Room> visited = new HashSet<>();

        List<Room> path = new ArrayList<>();
        path.add(from);
        queue.add(path);

        while (!queue.isEmpty()) {

            path = queue.poll();
            from = path.get(path.size() - 1);

            if (from.equals(to)) {
                break;
            }

            for (Room neighbour : from.getAdjacentRooms().values()) {
                if (!visited.contains(neighbour)) {
                    List<Room> pathToNextNode = new ArrayList<>(path);
                    pathToNextNode.add(neighbour);
                    queue.add(pathToNextNode);
                    visited.add(neighbour);
                }
            }
        }

        return getDirections(path);
    }

    private List<Directions> getDirections(List<Room> path) {
        List<Directions> directionsList = new ArrayList<>();
        Room currentRoom = path.get(0);

        for (int i = 1; i < path.size(); i++) {
            Room nextRoom = path.get(i);
            Map<Directions, Room> adjacentRooms = currentRoom.getAdjacentRooms();
            Directions direction = null;
            for (Map.Entry<Directions, Room> entry: adjacentRooms.entrySet()) {
                if(entry.getValue().equals(nextRoom)) {
                    direction = entry.getKey();
                }
            }
            directionsList.add(direction);
            currentRoom = nextRoom;
        }
        return directionsList;
    }

}
