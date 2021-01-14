package game;

import data.DungeonFixture;
import game.dungeon.Dungeon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import static game.DungeonGame.Messages.*;
import static game.DungeonGame.Messages.CHOICE;
import static game.DungeonGame.Messages.POSSIBLE_MOVES;

@ExtendWith(MockitoExtension.class)
class DungeonGameTest {

    @Test
    void shouldPrintCorrectOutputForUserInput() throws IOException {

        Dungeon dungeon = DungeonFixture.defaultDungeon();
        BufferedReader bufferedReaderMock = Mockito.mock(BufferedReader.class);
        PrintStream printStreamMock = Mockito.mock(PrintStream.class);
        Mockito.when(bufferedReaderMock.readLine()).thenReturn("n", "q");

        DungeonGame dungeonGame = new DungeonGame(dungeon, bufferedReaderMock, printStreamMock);
        dungeonGame.start();

        InOrder inOrder = Mockito.inOrder(printStreamMock);
        inOrder.verify(printStreamMock).println(TURN_DELIMETER);
        inOrder.verify(printStreamMock).print(
                "        a1--a2          \n" +
                "        |   |           \n" +
                "    a4--a3  a5--a6--a7  \n" +
                "    |   |       |   |   \n" +
                "b1--b0  **      a8--a9  \n" +
                "|               |       \n" +
                "b2--b3--b4--b5--b6      \n"
        );
        inOrder.verify(printStreamMock).println(PLAYER_IN_ROOM +  "a0");
        inOrder.verify(printStreamMock).println(POSSIBLE_MOVES + "n");
        inOrder.verify(printStreamMock).print(CHOICE);

        inOrder.verify(printStreamMock).println(TURN_DELIMETER);
        inOrder.verify(printStreamMock).print(
                "        a1--a2          \n" +
                        "        |   |           \n" +
                        "    a4--**  a5--a6--a7  \n" +
                        "    |   |       |   |   \n" +
                        "b1--b0  a0      a8--a9  \n" +
                        "|               |       \n" +
                        "b2--b3--b4--b5--b6      \n"
        );
        inOrder.verify(printStreamMock).println(PLAYER_IN_ROOM + "a3");
        inOrder.verify(printStreamMock).println(POSSIBLE_MOVES + "nsw");
        inOrder.verify(printStreamMock).print(CHOICE);
    }
}