package game.dungeon;

import game.Directions;
import game.dungeon.iterator.IllegalPositionIteratorMove;
import game.Room;
import game.dungeon.iterator.PositionIterable;
import game.dungeon.iterator.PositionIterator;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Dungeon implements PositionIterable {

    private final Room startingRoom;
    private final Set<Room> allRooms;

    public Dungeon(Room startingRoom) {
        this.startingRoom = startingRoom;
        this.allRooms = flatten(startingRoom);
    }

    @Override
    public PositionIterator positionIterator() {
        return new DungeonPositionIterator(startingRoom);
    }

    private Set<Room> flatten(Room room) {
        return new HashSet<>(traverse(room));
    }

    private Set<Room> traverse(Room room) {

        Map<Directions, Room> adjacentRooms = room.getAdjacentRooms();

        Queue<Room> queue = new ArrayDeque<>(adjacentRooms.values());
        Set<Room> visitedRooms = new HashSet<>();
        visitedRooms.add(room);

        while (!queue.isEmpty()) {
            Room poll = queue.poll();
            if (!visitedRooms.contains(poll)) {
                visitedRooms.add(poll);
                queue.addAll(poll.getAdjacentRooms().values());
            }
        }
        return visitedRooms;
    }

    public Set<Room> getRooms() {
        return allRooms;
    }

    public Room getStartingRoom() {
        return startingRoom;
    }

    private static class DungeonPositionIterator implements PositionIterator {

        private Room currentRoom;

        public DungeonPositionIterator(Room currentRoom) {
            this.currentRoom = currentRoom;
        }

        @Override
        public Set<Directions> possibleMoves() {
            return new HashSet<>(currentRoom.getAdjacentRooms().keySet());
        }

        @Override
        public void move(Directions direction) {
            currentRoom = currentRoom.getAdjacentRooms()
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getKey() == direction)
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElseThrow(() -> new IllegalPositionIteratorMove("Cannot move to " + direction + " from room " + currentRoom.getName()));
        }

        @Override
        public Room currentRoom() {
            return currentRoom;
        }

        @Override
        public PositionIterator copy() {
            return new DungeonPositionIterator(this.currentRoom);
        }

        @Override
        public boolean isValidMove(Directions directions) {
            return possibleMoves().contains(directions);
        }
    }
}
