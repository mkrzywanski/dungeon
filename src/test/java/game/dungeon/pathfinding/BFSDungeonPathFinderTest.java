package game.dungeon.pathfinding;

import data.DungeonFixture;
import game.Directions;
import game.Room;
import game.dungeon.Dungeon;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BFSDungeonPathFinderTest {

    @Test
    void shouldFindShortestPath() {
        Dungeon dungeon = DungeonFixture.defaultDungeon();
        BFSDungeonPathFinder bfsDungeonPathFinder = new BFSDungeonPathFinder();

        Room from = dungeon.getStartingRoom();
        Room to = dungeon.getRooms()
                .stream()
                .filter(room -> room.getName().equals("b3"))
                .findAny()
                .orElseThrow();

        List<Directions> shortestPath = bfsDungeonPathFinder.findShortestPath(dungeon.getRooms(), from, to);

        List<Directions> expectedMoves = List.of(
                Directions.N,
                Directions.W,
                Directions.S,
                Directions.W,
                Directions.S,
                Directions.E);
        System.out.println(shortestPath);
        assertThat(shortestPath).isEqualTo(expectedMoves);
    }

}