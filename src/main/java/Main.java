import game.DungeonGame;
import game.dungeon.Dungeon;
import game.dungeon.loading.DungeonLoader;
import game.dungeon.loading.DungeonLoadingException;
import game.dungeon.loading.SimpleDungeonLoader;
import parsing.Entry;
import parsing.InputParser;
import parsing.ParsingException;
import parsing.StringInputParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

//things to be done :
//- handle closing input stream that are based on System.in/out
//- change implementation of DungeonGame and decouple input parsing, logic execution (CommandParser, Command, CommandFactory) classes
//- change implementation of DungeonGame to print to output only once and build the output during single loop execution which would make tests of this class more readable.
public class Main {
    public static void main(String[] args) throws DungeonLoadingException, IOException, ParsingException, URISyntaxException {

        List<String> lines = readInitialDungeon();

        InputParser inputParser = new StringInputParser(lines);
        List<Entry> entries = inputParser.parse();

        DungeonLoader dungeonLoader = new SimpleDungeonLoader(entries);
        Dungeon dungeon = dungeonLoader.load();

        DungeonGame dungeonGame = new DungeonGame(dungeon, new BufferedReader(new InputStreamReader(System.in)), System.out);
        dungeonGame.start();
    }

    private static List<String> readInitialDungeon() throws IOException {
        InputStream in = Main.class.getResourceAsStream("dungeon.txt");
        List<String> result = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = bf.readLine()) != null) {
                result.add(line);
            }
        }
        return result;
    }
}