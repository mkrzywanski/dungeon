package game.dungeon.loading;

import game.dungeon.Dungeon;

public interface DungeonLoader {
    Dungeon load() throws DungeonLoadingException;
}
