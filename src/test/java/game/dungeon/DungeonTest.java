package game.dungeon;

import data.DungeonFixture;
import game.Directions;
import game.Room;
import game.dungeon.iterator.IllegalPositionIteratorMove;
import game.dungeon.iterator.PositionIterator;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class DungeonTest {

    @Test
    void iteratorShouldBePlacedOnStartingPositionWhenCreated() {
        Dungeon dungeon = DungeonFixture.defaultDungeon();
        PositionIterator positionIterator = dungeon.positionIterator();

        Room room = positionIterator.currentRoom();
        assertThat(room.getName()).isEqualTo("a0");
        assertThat(room.getAdjacentRooms()).hasSize(1);

        Set<Directions> possibleMoves = positionIterator.possibleMoves();
        assertThat(possibleMoves).hasSize(1);
        assertThat(possibleMoves).containsExactly(Directions.N);
    }

    @Test
    void shouldMove() {
        Dungeon dungeon = DungeonFixture.defaultDungeon();
        PositionIterator positionIterator = dungeon.positionIterator();

        positionIterator.move(Directions.N);
        positionIterator.move(Directions.N);
        positionIterator.move(Directions.E);
        positionIterator.move(Directions.S);
        positionIterator.move(Directions.E);
        positionIterator.move(Directions.E);
        positionIterator.move(Directions.S);
        positionIterator.move(Directions.W);
        positionIterator.move(Directions.S);
        positionIterator.move(Directions.W);
        positionIterator.move(Directions.W);
        positionIterator.move(Directions.W);
        positionIterator.move(Directions.W);
        positionIterator.move(Directions.N);
        positionIterator.move(Directions.E);
        positionIterator.move(Directions.N);
        positionIterator.move(Directions.E);
        positionIterator.move(Directions.S);

        assertThat(positionIterator.currentRoom().getName()).isEqualTo("a0");
    }

    @Test
    void shouldThrowExceptionWhenMoveIsImpossible() {
        Dungeon dungeon = DungeonFixture.defaultDungeon();
        PositionIterator positionIterator = dungeon.positionIterator();

        ThrowableAssert.ThrowingCallable callable = () -> positionIterator.move(Directions.W);

        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalPositionIteratorMove.class);
    }
}