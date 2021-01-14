package game.dungeon.formatting;

import data.DungeonFixture;
import game.dungeon.Dungeon;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleDungeonFormatterTest {

    private String expectedFormattedDungeon =
            "        a1--a2          \n" +
            "        |   |           \n" +
            "    a4--a3  a5--a6--a7  \n" +
            "    |   |       |   |   \n" +
            "b1--b0  a0      a8--a9  \n" +
            "|               |       \n" +
            "b2--b3--b4--b5--b6      \n";

    private DungeonFormatter dungeonFormatter = new SimpleDungeonFormatter();

    @Test
    void shouldFormatDefaultDungeon() {
        Dungeon dungeon = DungeonFixture.defaultDungeon();

        String format = dungeonFormatter.format(dungeon);

        assertThat(format).isEqualTo(expectedFormattedDungeon);
    }
}