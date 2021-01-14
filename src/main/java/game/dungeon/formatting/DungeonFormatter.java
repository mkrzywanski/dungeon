package game.dungeon.formatting;

import game.dungeon.Dungeon;

public interface DungeonFormatter {
    String format(Dungeon dungeon);
    String formatWithPlayer(Dungeon dungeon, String playerPosition);
}
