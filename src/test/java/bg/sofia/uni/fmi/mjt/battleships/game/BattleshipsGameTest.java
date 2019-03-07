package bg.sofia.uni.fmi.mjt.battleships.game;

import bg.sofia.uni.fmi.mjt.battleships.enums.GameState;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BattleshipsGameTest {
    @Mock
    private BattleshipsServer serverMock;

    private String gameName = "testGame";
    private String creator = "some name";
    private BattleshipsGame game;

    //region String Constants
    private static final String turnSeparator =
            "   _ _ _ _ _ _ _ _ _   " + System.lineSeparator() +
                    "*| _ _ Next Turn _ _ |*" + System.lineSeparator();

    private static final String exampleBoard =
            "       YOUR BOARD" + System.lineSeparator() +
                    "   1 2 3 4 5 6 7 8 9 10" + System.lineSeparator() +
                    "   _ _ _ _ _ _ _ _ _ _" + System.lineSeparator() +
                    "A |X|O|X|X|X|X|_|_|_|_|" + System.lineSeparator() +
                    "B |X|_|_|_|_|_|_|_|_|_|" + System.lineSeparator() +
                    "C |_|_|_|_|_|X|X|X|X|X|" + System.lineSeparator() +
                    "D |_|X|X|_|_|X|X|_|_|_|" + System.lineSeparator() +
                    "E |_|_|_|_|_|X|X|_|_|_|" + System.lineSeparator() +
                    "F |_|_|_|_|_|X|X|X|_|_|" + System.lineSeparator() +
                    "G |_|_|_|_|_|X|X|X|X|_|" + System.lineSeparator() +
                    "H |_|X|_|_|_|_|X|_|X|_|" + System.lineSeparator() +
                    "I |_|X|_|_|_|_|_|_|*|_|" + System.lineSeparator() +
                    "J |_|X|_|_|_|_|_|_|_|_|" + System.lineSeparator() +
                    System.lineSeparator() +
                    "      ENEMY BOARD" + System.lineSeparator() +
                    "   1 2 3 4 5 6 7 8 9 10" + System.lineSeparator() +
                    "   _ _ _ _ _ _ _ _ _ _" + System.lineSeparator() +
                    "A |O|O|_|O|_|O|_|_|_|_|" + System.lineSeparator() +
                    "B |_|_|_|X|_|_|_|_|_|_|" + System.lineSeparator() +
                    "C |_|_|_|_|_|O|O|O|O|O|" + System.lineSeparator() +
                    "D |_|X|X|_|_|_|O|_|_|_|" + System.lineSeparator() +
                    "E |_|_|_|_|_|O|O|_|_|_|" + System.lineSeparator() +
                    "F |_|_|_|_|_|O|O|X|_|_|" + System.lineSeparator() +
                    "G |_|_|_|_|X|O|O|O|O|_|" + System.lineSeparator() +
                    "H |_|O|_|_|O|_|O|_|O|_|" + System.lineSeparator() +
                    "I |_|_|_|_|_|_|_|_|O|_|" + System.lineSeparator() +
                    "J |_|O|_|_|_|_|_|_|_|_|" + System.lineSeparator() +
                    System.lineSeparator();
    //endregion

    @Before
    public void setup() {
        game = new BattleshipsGame(gameName, creator, serverMock);
    }

    @Test
    public void testJoinGameFirstJoining() {
        BattleshipsUserData player = mock(BattleshipsUserData.class);
        game.join(player);

        final int expectedSize = 1;
        assertEquals(expectedSize, game.getPlayerCount());
    }

    @Test
    public void testJoinGameTwoJoins() {
        BattleshipsUserData playerOne = mock(BattleshipsUserData.class);
        game.join(playerOne);
        BattleshipsUserData playerTwo = mock(BattleshipsUserData.class);
        game.join(playerTwo);

        final int expectedSize = 2;
        assertEquals(expectedSize, game.getPlayerCount());
        assertEquals(GameState.AWAITING_START, game.getGameState());
    }

    @Test
    public void testEnterGameQueueSingleEnter() {
        BattleshipsUserData playerOne = mock(BattleshipsUserData.class);

        final int outputStreamSize = 1024;
        ByteArrayOutputStream playerOutputStream = new
                ByteArrayOutputStream(outputStreamSize);
        PrintWriter writer = new PrintWriter(playerOutputStream, true);

        when(playerOne.getWriter()).thenReturn(writer);

        game.enterGameQueue(playerOne);

        final int expectedSize = 1;
        assertEquals(expectedSize, game.getPlayersInQueue());
        assertEquals("Waiting for other player to type <start>" +
                        System.lineSeparator(),
                playerOutputStream.toString());
    }

    @Test
    public void testEnterGameQueueTwoEntries() {
        BattleshipsUserData playerOne = mock(BattleshipsUserData.class);
        BattleshipsUserData playerTwo = mock(BattleshipsUserData.class);

        String expectedPlayerOneResponse =
                "Waiting for other player to type <start>" +
                        System.lineSeparator() +
                        turnSeparator;

        final int playerOneOutputStreamSize = expectedPlayerOneResponse.length();
        ByteArrayOutputStream playerOneOutputStream = new
                ByteArrayOutputStream(playerOneOutputStreamSize);
        PrintWriter playerOneWriter = new
                PrintWriter(playerOneOutputStream, true);

        String expectedPlayerTwoResponse = turnSeparator;

        final int playerTwoOutputStreamSize = expectedPlayerTwoResponse.length();
        ByteArrayOutputStream playerTwoOutputStream = new
                ByteArrayOutputStream(playerTwoOutputStreamSize);
        PrintWriter playerTwoWriter = new
                PrintWriter(playerTwoOutputStream, true);

        when(playerOne.getWriter()).thenReturn(playerOneWriter);

        when(playerTwo.getWriter()).thenReturn(playerTwoWriter);

        game.join(playerOne);
        game.join(playerTwo);

        game.enterGameQueue(playerOne);
        game.enterGameQueue(playerTwo);

        final int expectedSize = 2;
        assertEquals(expectedSize, game.getPlayersInQueue());

        assertEquals(expectedPlayerOneResponse,
                playerOneOutputStream.toString()
                        .substring(0, playerOneOutputStreamSize));

        assertEquals(expectedPlayerTwoResponse,
                playerTwoOutputStream.toString()
                        .substring(0, playerTwoOutputStreamSize));
    }

//    @Test
//    public void testAttackPlayerSuccessfulAttack() throws
//            CloneNotSupportedException {
//        BattleshipsUserData playerOne = mock(BattleshipsUserData.class);
//        BattleshipsUserData playerTwo = mock(BattleshipsUserData.class);
//
//        String attackPosition = "B4";
//        int playerOneResponseLengthCut =
//                "Waiting for other player to type <start>".length() +
//                        System.lineSeparator().length() * 3 +
//                        turnSeparator.length() * 2 +
//                        exampleBoard.length()  * 2;
//
//        String expectedPlayerOneResponse = "Hit enemy ship at: " + attackPosition +
//                System.lineSeparator() + System.lineSeparator();
//
//        final int playerOneOutputStreamSize = playerOneResponseLengthCut +
//                expectedPlayerOneResponse.length();
//        ByteArrayOutputStream playerOneOutputStream = new
//                ByteArrayOutputStream(playerOneOutputStreamSize);
//        PrintWriter playerOneWriter = new
//                PrintWriter(playerOneOutputStream, true);
//
//        int playerTwoResponseLengthCut =
//                    System.lineSeparator().length() * 3 +
//                    turnSeparator.length() * 2 +
//                    exampleBoard.length()  * 2;
//
//        String expectedPlayerTwoResponse = "Got hit by enemy at: " + attackPosition +
//                System.lineSeparator() + System.lineSeparator();
//
//        final int playerTwoOutputStreamSize = 1024;
//        ByteArrayOutputStream playerTwoOutputStream = new
//                ByteArrayOutputStream(playerTwoOutputStreamSize);
//        PrintWriter playerTwoWriter = new
//                PrintWriter(playerTwoOutputStream, true);
//
//        when(playerOne.getWriter()).thenReturn(playerOneWriter);
//        when(playerTwo.getWriter()).thenReturn(playerTwoWriter);
//
//        Board boardMock = mock(Board.class);
//
//        when(boardMock.attack(attackPosition)).thenReturn(true);
//        when(boardMock.allShipsAreSunk()).thenReturn(false);
//        when(boardMock.clone()).thenReturn(boardMock);
//        when(boardMock.toString(anyBoolean())).thenReturn(exampleBoard);
//
//        SavedGame savedGameMock = mock(SavedGame.class);
//        when(savedGameMock.getGameName()).thenReturn(gameName);
//        when(savedGameMock.getCreator()).thenReturn(playerOne);
//        when(savedGameMock.getCreatorBoard()).thenReturn(boardMock);
//        when(savedGameMock.getOtherBoard()).thenReturn(boardMock);
//
//        game = new BattleshipsGame(savedGameMock);
//
//        game.join(playerTwo);
//
//        game.enterGameQueue(playerOne);
//        game.enterGameQueue(playerTwo);
//
//        game.attackPlayer(playerTwo, attackPosition);
//
//        assertEquals(expectedPlayerOneResponse,
//                playerOneOutputStream.toString());
////                        .substring(playerOneResponseLengthCut));
//
//        assertEquals(expectedPlayerTwoResponse,
//                playerTwoOutputStream.toString()
//                        .substring(playerTwoResponseLengthCut));
//    }
}
