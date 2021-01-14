package game.dungeon.loading;

import game.Directions;
import game.Room;
import game.dungeon.Dungeon;
import parsing.Entry;
import parsing.Passage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleDungeonLoader implements DungeonLoader {

    private static final String STARTING_ROOM_NAME = "a0";
    private final List<Entry> entries;

    public SimpleDungeonLoader(final List<Entry> entries) {
        this.entries = entries;
    }

    @Override
    public Dungeon load() throws DungeonLoadingException {

        Entry startingEntry = entries.stream()
                .filter(entry -> entry.getRoomName().equals(STARTING_ROOM_NAME))
                .findFirst()
                .orElseThrow(() -> new DungeonLoadingException("no starting position defined"));

        Map<String, Room> existingRooms = new HashMap<>();

        Map<Directions, Room> startingRoomPassages = getStartingRoomPassages(startingEntry);

        for (Room room : startingRoomPassages.values()) {
            existingRooms.put(room.getName(), room);
        }

        Room startingRoom = new Room(STARTING_ROOM_NAME, startingRoomPassages);
        existingRooms.put(startingRoom.getName(), startingRoom);

        List<Entry> otherRoomEntries = entries.stream()
                .filter(entry -> !entry.getRoomName().equals(STARTING_ROOM_NAME))
                .collect(Collectors.toList());

        fillExistingRooms(existingRooms, otherRoomEntries);

        return new Dungeon(startingRoom);

    }

    private void fillExistingRooms(Map<String, Room> existingRooms, List<Entry> otherRoomEntries) {
        for (Entry entry : otherRoomEntries) {
            Room currentRoom;
            if (existingRooms.containsKey(entry.getRoomName())) {
                currentRoom = existingRooms.get(entry.getRoomName());
            } else {
                currentRoom = new Room(entry.getRoomName());
                existingRooms.put(entry.getRoomName(), currentRoom);
            }

            Set<Passage> passages = entry.getPassageSet();

            for (Passage passage : passages) {
                String destinationRoom = passage.getDestinationRoom();
                Room destRoom;
                if (!existingRooms.containsKey(destinationRoom)) {
                    destRoom = new Room(passage.getDestinationRoom());
                    existingRooms.put(destinationRoom, destRoom);
                } else {
                    destRoom = existingRooms.get(destinationRoom);
                }
                currentRoom.insertRoom(Directions.from(passage.getDirection()), destRoom);
            }
        }
    }

    private Map<Directions, Room> getStartingRoomPassages(Entry startingEntry) {
        //handle parsing direction
        return startingEntry.getPassageSet().stream()
                .collect(Collectors.toMap(o -> Directions.from(o.getDirection()), passage -> new Room(passage.getDestinationRoom(), new HashMap<>())));
    }
}
