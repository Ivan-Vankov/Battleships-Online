package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreateGameCommandTest {
    private Socket socketMock = mock(Socket.class);

    private BufferedReader readerMock = mock(BufferedReader.class);

    private BattleshipsServer serverMock = mock(BattleshipsServer.class);

    private String userName = "waffle";
    private BattleshipsUserData userData;
    private static final int OUTPUT_STREAM_SIZE = 1000;
    private ByteArrayOutputStream userOutputStream;
    private PrintWriter writer;

    private CreateGameCommand createGameCommand;

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
    public void testCreateGameUnusedGameName() {
        String gameName = "testGame";
        userData = new BattleshipsUserData(
                userName, socketMock, readerMock, writer);

        when(serverMock.hasGame(gameName)).thenReturn(false);

        createGameCommand = new CreateGameCommand(userData, serverMock);

        createGameCommand.execute(gameName);

        assertEquals("Game <" + gameName + "> created, players 1/2" +
                        System.lineSeparator(),
                userOutputStream.toString());
    }

    @Test
    public void testCreateGameGameNameInUse() {
        String gameName = "testGame";

        when(serverMock.hasGame(gameName)).thenReturn(true);

        userData = new BattleshipsUserData(
                userName, socketMock, readerMock, writer);


        when(serverMock.hasGame(gameName)).thenReturn(true);

        createGameCommand = new CreateGameCommand(userData, serverMock);

        createGameCommand.execute(gameName);

        assertEquals("Game name <" + gameName + "> taken" +
                        System.lineSeparator(),
                userOutputStream.toString());
    }

    @Test
    public void testCreateGameEmptyGameName() {
        String gameName = "";

        userData = new BattleshipsUserData(
                userName, socketMock, readerMock, writer);

        createGameCommand = new CreateGameCommand(userData, serverMock);

        createGameCommand.execute(gameName);

        assertEquals("Please give a game name" +
                        System.lineSeparator(),
                userOutputStream.toString());
    }
}
