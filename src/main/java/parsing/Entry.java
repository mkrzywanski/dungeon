package parsing;

import java.util.Set;

public class Entry {
    private String roomName;
    private Set<Passage> passageSet;

    public Entry(String roomName, Set<Passage> passageSet) {
        this.roomName = roomName;
        this.passageSet = passageSet;
    }

    public String getRoomName() {
        return roomName;
    }

    public Set<Passage> getPassageSet() {
        return passageSet;
    }
}
