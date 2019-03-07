package bg.sofia.uni.fmi.mjt.battleships.runnables;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BattleshipsConnectionRunnableTest {

    @Mock
    private Socket socketMock;

    @Mock
    private BattleshipsServer serverMock;

    private String userName = "waffle";
    private BattleshipsUserData userData;
    private static final int OUTPUT_STREAM_SIZE = 1000;
    private ByteArrayOutputStream userOutputStream;
    private PrintWriter writer;

    private BattleshipsConnectionRunnable connectionRunnable;

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
    public void testRunUnknownCommand() {
        String unknownCommand = "unknown";
        ByteArrayInputStream commandStream =
                new ByteArrayInputStream(unknownCommand.getBytes());
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(commandStream));

        PrintWriter writer = new PrintWriter(userOutputStream, true);

        userData = new BattleshipsUserData(
                userName, socketMock, reader, writer);

        connectionRunnable = new BattleshipsConnectionRunnable(
                userData,
                serverMock);

        connectionRunnable.run();

        assertEquals("Unknown command" + System.lineSeparator(),
                userOutputStream.toString());
    }

    @Test
    public void testCreateGameUnusedGameName() {
        String gameName = "testGame";
        String command = "create-game " + gameName;
        ByteArrayInputStream commandStream =
                new ByteArrayInputStream(command.getBytes());
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(commandStream));

        userData = new BattleshipsUserData(
                userName, socketMock, reader, writer);

        connectionRunnable = new BattleshipsConnectionRunnable(
                userData,
                serverMock);

        connectionRunnable.run();

        assertEquals("Game <" + gameName + "> created, players 1/2" +
                        System.lineSeparator(),
                userOutputStream.toString());
        assertEquals(userData.getCurrentState(), ConnectionState.IN_LOBBY);
    }
}