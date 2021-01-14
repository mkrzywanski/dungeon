package game.dungeon.loading;

import data.DungeonFixture;
import game.Room;
import game.dungeon.Dungeon;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import parsing.Entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimpleDungeonLoaderTest {

    @Test
    void shouldConstructDungeon() throws DungeonLoadingException {
        DungeonLoader dungeonLoader = new SimpleDungeonLoader(DungeonFixture.defaultDungeonEntries());
        Dungeon load = dungeonLoader.load();

        assertThat(load.getStartingRoom().getName()).isEqualTo("a0");
        assertThat(load.getStartingRoom().getAdjacentRooms()).hasSize(1);
        assertThat(load.getRooms()).hasSize(17);
    }

    @Test
    void shouldThrowExceptionWhenThereIsNoA0Element() {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry("a1", new HashSet<>()));

        DungeonLoader dungeonLoader = new SimpleDungeonLoader(entries);
        ThrowableAssert.ThrowingCallable callable = dungeonLoader::load;

        assertThatThrownBy(callable).hasMessageContaining("no starting position defined");
    }
}