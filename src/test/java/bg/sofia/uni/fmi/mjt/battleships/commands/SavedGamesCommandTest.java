package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.game.Board;
import bg.sofia.uni.fmi.mjt.battleships.game.SavedGame;
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
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SavedGamesCommandTest {

    private Socket socketMock = mock(Socket.class);

    private BufferedReader readerMock = mock(BufferedReader.class);

    private BattleshipsServer serverMock = mock(BattleshipsServer.class);

    private String userName = "waffle";
    private BattleshipsUserData userData;
    private static final int OUTPUT_STREAM_SIZE = 1000;
    private ByteArrayOutputStream userOutputStream;
    private PrintWriter writer;

    private SavedGamesCommand savedGamesCommand;

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
    public void testSavedGamesWithTwoSavedGames() throws CloneNotSupportedException {
        Board boardMock = mock(Board.class);
        when(boardMock.clone()).thenReturn(boardMock);

        String game1Name = "game1";
        String game2Name = "game2";

        var savedGames = new HashMap<String, SavedGame>();
        savedGames.put(game1Name, new SavedGame(boardMock, boardMock, game1Name, userData));
        savedGames.put(game2Name, new SavedGame(boardMock, boardMock, game2Name, userData));

        when(serverMock.getSavedGames(userName)).thenReturn(savedGames);

        userData = new BattleshipsUserData(
                userName, socketMock, readerMock, writer);

        savedGamesCommand = new SavedGamesCommand(userData, serverMock);

        savedGamesCommand.execute("");

        assertEquals("Your saved games:" +
                        System.lineSeparator() +
                        "0: " + game1Name +
                        System.lineSeparator() +
                        "1: " + game2Name +
                        System.lineSeparator(),
                userOutputStream.toString());
    }

    @Test
    public void testSavedGamesWithNoSavedGames() {
        when(serverMock.getSavedGames(userName)).thenReturn(
                Collections.emptyMap()
        );

        userData = new BattleshipsUserData(
                userName, socketMock, readerMock, writer);

        savedGamesCommand = new SavedGamesCommand(userData, serverMock);

        savedGamesCommand.execute("");

        assertEquals("You have no saved games" +
                        System.lineSeparator(),
                userOutputStream.toString());
    }

    @Test
    public void testSavedGamesWithUnneededParameters() {
        userData = new BattleshipsUserData(
                userName, socketMock, readerMock, writer);

        savedGamesCommand = new SavedGamesCommand(userData, serverMock);

        savedGamesCommand.execute("1 2 3");

        assertEquals("Command saved-games " +
                        "doesn't take any parameters" +
                        System.lineSeparator(),
                userOutputStream.toString());
    }
}
