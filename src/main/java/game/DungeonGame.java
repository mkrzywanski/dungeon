package game;

import game.dungeon.Dungeon;
import game.dungeon.formatting.DungeonFormatter;
import game.dungeon.formatting.SimpleDungeonFormatter;
import game.dungeon.iterator.PositionIterator;
import game.dungeon.pathfinding.BFSDungeonPathFinder;
import game.dungeon.pathfinding.DungeonPathFinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static game.DungeonGame.Messages.*;
import static java.lang.String.format;

//we can introduce builder for game object that can set implementations of formatters, pathfinders, and input output streams
//and will fall back to defaults if needed.
public class DungeonGame {

    private static final String QUIT_BUTTON = "q";
    private static final Set<String> SUPPORTED_DIRECTIONS = Set.of("n", "s", "e", "w");
    private static final Set<String> SUPPORTED_KEYS = Set.of("n", "s", "e", "w", QUIT_BUTTON);
    private static final String FIND_COMMAND = "find";

    private final Dungeon dungeon;
    private final BufferedReader bufferedReader;
    private final PrintStream printStream;
    //this can be injected later by client so that we can switch formatting and path finding logic
    private final DungeonFormatter dungeonFormatter = new SimpleDungeonFormatter();
    private final DungeonPathFinder pathFinder = new BFSDungeonPathFinder();

    public DungeonGame(Dungeon dungeon, BufferedReader bufferedReader, PrintStream printStream) {
        this.dungeon = dungeon;
        this.bufferedReader = bufferedReader;
        this.printStream = printStream;
    }

    public void start() {
        PositionIterator positionIterator = dungeon.positionIterator();

        while (!Thread.currentThread().isInterrupted()) {
            printStream.println(TURN_DELIMETER);

            String board = dungeonFormatter.formatWithPlayer(dungeon, positionIterator.currentRoom().getName());
            printStream.print(board);

            String roomName = positionIterator.currentRoom().getName();
            printStream.println(PLAYER_IN_ROOM + roomName);

            Set<Directions> possibleMoves = positionIterator.possibleMoves();

            String possibleMovesOutput = possibleMoves
                    .stream()
                    .map(directions -> directions.name().toLowerCase())
                    .sorted()
                    .collect(Collectors.joining());

            printStream.println(POSSIBLE_MOVES + possibleMovesOutput);
            printStream.print(CHOICE);

            String input;
            try {
                input = bufferedReader.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                break;
            }

            if (input.startsWith(FIND_COMMAND)) {
                handleFindCommand(positionIterator, input);
            } else if (input.length() > 1) {
                handleMultipleDirections(positionIterator, input);
            } else if (!SUPPORTED_KEYS.contains(input)) {
                printStream.println(UNSUPPORTED_INPUT + input);
            } else if (input.equals(QUIT_BUTTON)) {
                printStream.println(QUIT);
                break;
            } else {
                handleSingleMove(positionIterator, input);
            }
        }
    }

    private void handleSingleMove(PositionIterator positionIterator, String input) {
        Directions nextDirection = Directions.from(input);
        if (!positionIterator.possibleMoves().contains(nextDirection)) {
            printStream.println(CANNOT_MOVE + nextDirection);
        } else {
            positionIterator.move(nextDirection);

        }
    }

    private void handleMultipleDirections(PositionIterator positionIterator, String input) {
        String[] moves = input.split("");
        boolean isValidSequence = verifyMoves(moves);
        if (!isValidSequence) {
            printStream.println(INVALID_SEQUENCE);
            return;
        }
        boolean isValidSequenceOfMoves = tryMove(positionIterator, moves);
        if (isValidSequenceOfMoves) {
            move(positionIterator, moves);
        } else {
            printStream.println(NOT_VALID_SEQUENCE);
        }
    }

    private void handleFindCommand(PositionIterator positionIterator, String input) {
        String[] findCommand = input.split(" ");
        if (findCommand.length == 1) {
            printStream.println(FIND_CMD_NO_ARGS);
            return;
        }
        String targetName = findCommand[1];
        printStream.println(format(FINDING_PATH, positionIterator.currentRoom().getName(), targetName));

        Room target;
        try {
            target = dungeon.getRooms()
                    .stream()
                    .filter(room -> room.getName().equals(targetName))
                    .findAny()
                    .orElseThrow(RuntimeException::new);
        } catch (RuntimeException e) {
            printStream.println(TARGET_ROOM_NOT_FOUND);
            return;
        }

        List<Directions> shortestPath = pathFinder.findShortestPath(dungeon.getRooms(), positionIterator.currentRoom(), target);
        String resultPath = shortestPath.stream()
                .map(direction -> direction.name().toLowerCase())
                .collect(Collectors.joining());

        printStream.println(PATH + resultPath);
    }

    private void move(PositionIterator positionIterator, String[] moves) {
        for (String move : moves) {
            Directions directions = Directions.from(move);
            positionIterator.move(directions);
        }
    }

    private boolean tryMove(PositionIterator positionIterator, String[] moves) {
        PositionIterator copy = positionIterator.copy();
        for (String move : moves) {
            Directions directions = Directions.from(move);
            boolean validMove = copy.isValidMove(directions);
            if (!validMove) {
                return false;
            } else {
                copy.move(directions);
            }
        }
        return true;
    }

    private boolean verifyMoves(String[] moves) {
        for (String move : moves) {
            if (!SUPPORTED_DIRECTIONS.contains(move)) {
                return false;
            }
        }
        return true;
    }

    public static final class Messages {
        public static final String TURN_DELIMETER = "------------------------------";
        public static final String CHOICE = "your choice: ";
        public static final String UNSUPPORTED_INPUT = "Unsupported input detected : ";
        public static final String QUIT = "Quit";
        public static final String CANNOT_MOVE = "Cannot move to ";
        public static final String INVALID_SEQUENCE = "Input contains not valid sequence";
        public static final String PLAYER_IN_ROOM = "you are in room ";
        public static final String POSSIBLE_MOVES = "possible moves: ";
        public static final String NOT_VALID_SEQUENCE = "Not Valid sequence";
        public static final String FIND_CMD_NO_ARGS = "find command has no argument";
        public static final String PATH = "the path is: ";
        public static final String FINDING_PATH = "finding path from %s to %s";
        public static final String TARGET_ROOM_NOT_FOUND = "target room does not exist";
    }
}
