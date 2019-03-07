package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.game.BattleshipsGame;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListGamesCommandTest {

    private Socket socketMock = mock(Socket.class);

    private BufferedReader readerMock = mock(BufferedReader.class);

    private BattleshipsServer serverMock = mock(BattleshipsServer.class);

    private String userName = "waffle";
    private BattleshipsUserData userData;
    private static final int OUTPUT_STREAM_SIZE = 1000;
    private ByteArrayOutputStream userOutputStream;
    private PrintWriter writer;

    private ListGamesCommand listGamesCommand;

    @Before
    public void setup() {
        userOutputStream = new ByteArrayOutputStream(
                OUTPUT_STREAM_SIZE);

        writer = new PrintWriter(userOutputStream, true);
    }

    @After
    public void cleanup() throws IOException {
        userOutputStream.close();
        writer.close();
    }

    @Test
    public void testListGamesWithTwoGames() {
        userData = new BattleshipsUserData(
                userName, socketMock, readerMock, writer);

        listGamesCommand = new ListGamesCommand(userData, serverMock);

        Map<String, BattleshipsGame> games = new HashMap<>();
        String game1Name = "game1";
        var game1 = new BattleshipsGame(game1Name, userName, serverMock);
        game1.join(userData);
        games.put(game1Name, game1);

        String game2Name = "longeeeeerGame2";
        games.put(game2Name, new BattleshipsGame(game2Name, userName, serverMock));

        when(serverMock.getAllGames()).thenReturn(games.values());

        listGamesCommand.execute("");

        assertEquals(
                "| NAME            | CREATOR | STATUS  | PLAYERS |" +
                        System.lineSeparator() +
                        "|-----------------+---------+---------+---------|" +
                        System.lineSeparator() +
                        "| game1           | waffle  | pending | 1/2     |" +
                        System.lineSeparator() +
                        "| longeeeeerGame2 | waffle  | pending | 0/2     |" +
                        System.lineSeparator(),
                userOutputStream.toString());
    }

    @Test
    public void testListGamesWithNoGames() {
        userData = new BattleshipsUserData(
                userName, socketMock, readerMock, writer);

        listGamesCommand = new ListGamesCommand(userData, serverMock);

        Map<String, BattleshipsGame> games = new HashMap<>();
        when(serverMock.getAllGames()).thenReturn(games.values());

        listGamesCommand.execute("");

        assertEquals(
                "There are no currently active games" +
                        System.lineSeparator(),
                userOutputStream.toString());
    }

    @Test
    public void testListGamesWithUnneededParameters() {
        userData = new BattleshipsUserData(
                userName, socketMock, readerMock, writer);

        listGamesCommand = new ListGamesCommand(userData, serverMock);
        listGamesCommand.execute("1 2 3");

        assertEquals("Command <list-games> " +
                        "doesn't take any parameters" +
                        System.lineSeparator(),
                userOutputStream.toString());
    }
}
