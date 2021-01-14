package game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Room {
    private final String name;
    private final Map<Directions, Room> adjacentRooms;

    public Room(String name, Map<Directions, Room> adjacentRooms) {
        this.name = name;
        this.adjacentRooms = adjacentRooms;
    }

    public Room(String name) {
        this.name = name;
        this.adjacentRooms = new HashMap<>();
    }

    public void insertRoom(Directions direction, Room room) {
        adjacentRooms.put(direction, room);
    }

    public String getName() {
        return name;
    }

    public Map<Directions, Room> getAdjacentRooms() {
        return Collections.unmodifiableMap(adjacentRooms);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return name.equals(room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
