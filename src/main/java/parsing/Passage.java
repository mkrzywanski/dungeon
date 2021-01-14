package parsing;

import java.util.Objects;

public class Passage {
    private String direction;
    private String destinationRoom;

    public Passage(String direction, String destinationRoom) {
        this.direction = direction;
        this.destinationRoom = destinationRoom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passage)) return false;
        Passage passage = (Passage) o;
        return getDirection().equals(passage.getDirection()) &&
                getDestinationRoom().equals(passage.getDestinationRoom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDirection(), getDestinationRoom());
    }

    public String getDirection() {
        return direction;
    }

    public String getDestinationRoom() {
        return destinationRoom;
    }
}
