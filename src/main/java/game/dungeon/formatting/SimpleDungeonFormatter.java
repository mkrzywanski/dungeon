package game.dungeon.formatting;

import game.Directions;
import game.Room;
import game.dungeon.Dungeon;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleDungeonFormatter implements DungeonFormatter {

    private static final String PLAYER_ICON = "**";

    @Override
    public String format(Dungeon dungeon) {
        List<Room> allRooms = new ArrayList<>(dungeon.getRooms());
        Set<Room> visited = new HashSet<>();

        Room firstRoom = allRooms.get(0);

        Map<Room, Index> roomToIndex = initializeRoomIndexes(firstRoom);
        visited.add(firstRoom);

        Queue<Room> queue = new ArrayDeque<>(firstRoom.getAdjacentRooms().values());

        while (!queue.isEmpty()) {
            Room currentRoom = queue.poll();
            if (visited.contains(currentRoom)) {
                continue;
            }
            Index parentIndex = roomToIndex.get(currentRoom);
            Map<Directions, Room> adjacentRooms = currentRoom.getAdjacentRooms();
            for (Map.Entry<Directions, Room> entry : adjacentRooms.entrySet()) {
                if (roomToIndex.containsKey(entry.getValue())) {
                    continue;
                }
                Directions direction = entry.getKey();
                Index index = newIndex(parentIndex, direction);
                roomToIndex.put(entry.getValue(), index);
            }
            for (Room room : currentRoom.getAdjacentRooms().values()) {
                if (!visited.contains(room) && !room.equals(currentRoom)) {
                    queue.add(room);
                }
            }
            visited.add(currentRoom);
        }

        return printDungeon(roomToIndex);

    }

    @Override
    public String formatWithPlayer(Dungeon dungeon, String playerPosition) {
        String format = format(dungeon);
        return format.replace(playerPosition, PLAYER_ICON);
    }

    private String printDungeon(Map<Room, Index> roomToIndex) {

        int minX = findMinimumX(roomToIndex.values());
        int minY = findMinimumY(roomToIndex.values());

        int maxX = findMaxX(roomToIndex.values());
        int maxY = findMaxY(roomToIndex.values());

        Map<Index, Room> collect = roomToIndex.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        StringBuilder stringBuilder = new StringBuilder();

        for (int j = maxY; j >= minY; j--) {
            Set<Integer> indexesOfVerticalLines = new HashSet<>();
            for (int i = minX; i <= maxX; i++) {
                Index index = new Index(i, j);
                if (collect.containsKey(index)) {
                    Room room = collect.get(index);
                    stringBuilder.append(room.getName());
                    if (room.getAdjacentRooms().containsKey(Directions.E)) {
                        stringBuilder.append("--");
                    } else {
                        stringBuilder.append("  ");
                    }
                    if (room.getAdjacentRooms().containsKey(Directions.S)) {
                        indexesOfVerticalLines.add(i);
                    }
                } else {
                    stringBuilder.append("    ");
                }
            }
            stringBuilder.append("\n");
            if (j == minY) {
                continue;
            }
            for (int i = minX; i <= maxX; i++) {
                if (indexesOfVerticalLines.contains(i)) {
                    stringBuilder.append("|   ");
                } else {
                    stringBuilder.append("    ");
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private Index newIndex(Index oldIndex, Directions direction) {
        int x = oldIndex.x;
        int y = oldIndex.y;
        switch (direction) {
            case E:
                x += 1;
                break;
            case N:
                y += 1;
                break;
            case S:
                y -= 1;
                break;
            case W:
                x -= 1;
                break;
        }
        return new Index(x, y);
    }

    private int findMinimumX(Collection<Index> values) {
        return values.stream().mapToInt(index -> index.x).min().orElse(0);
    }

    private int findMinimumY(Collection<Index> values) {
        return values.stream().mapToInt(index -> index.y).min().orElse(0);
    }

    private int findMaxX(Collection<Index> values) {
        return values.stream().mapToInt(index -> index.x).max().orElse(0);
    }

    private int findMaxY(Collection<Index> values) {
        return values.stream().mapToInt(index -> index.y).max().orElse(0);
    }

    private Map<Room, Index> initializeRoomIndexes(Room room1) {
        Map<Room, Index> roomToIndex = new HashMap<>();
        roomToIndex.put(room1, new Index(0, 0));
        Map<Directions, Room> adjacentRooms = room1.getAdjacentRooms();
        for (Map.Entry<Directions, Room> adjacentRoom : adjacentRooms.entrySet()) {
            Index newIndex = newIndex(new Index(0, 0), adjacentRoom.getKey());
            roomToIndex.put(adjacentRoom.getValue(), newIndex);
        }
        return roomToIndex;
    }

    private static class Index {

        private final int x;
        private final int y;

        public Index(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Index)) return false;
            Index index = (Index) o;
            return x == index.x &&
                    y == index.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
