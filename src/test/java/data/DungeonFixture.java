package data;

import game.dungeon.Dungeon;
import game.dungeon.loading.DungeonLoader;
import game.dungeon.loading.DungeonLoadingException;
import game.dungeon.loading.SimpleDungeonLoader;
import parsing.Entry;
import parsing.InputParser;
import parsing.ParsingException;
import parsing.StringInputParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DungeonFixture {
    public static Dungeon defaultDungeon() {
        try {
            List<Entry> entries = defaultDungeonEntries();
            DungeonLoader dungeonLoader = new SimpleDungeonLoader(entries);
            return dungeonLoader.load();
        } catch (DungeonLoadingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static List<Entry> defaultDungeonEntries() {
        try {
            List<String> lines = defaultDungeonFileLines();
            InputParser inputParser = new StringInputParser(lines);
            return inputParser.parse();
        } catch (ParsingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static List<String> defaultDungeonFileLines() {
        try {
            return Files.readAllLines(Paths.get("src/test/resources/dungeon.txt"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
