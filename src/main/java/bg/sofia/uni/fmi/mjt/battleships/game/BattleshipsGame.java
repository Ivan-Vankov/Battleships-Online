package bg.sofia.uni.fmi.mjt.battleships.game;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.enums.GameState;
import bg.sofia.uni.fmi.mjt.battleships.exceptions.GameQueueFullException;
import bg.sofia.uni.fmi.mjt.battleships.exceptions.GameSessionFullException;
import bg.sofia.uni.fmi.mjt.battleships.exceptions.PlayerNotInGameException;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

import java.util.Arrays;
import java.util.Collection;

public class BattleshipsGame {
    public static final int MAX_PLAYER_COUNT = 2;

    private BattleshipsUserData playerOne;
    private Board playerOneBoard;

    private BattleshipsUserData playerTwo;
    private Board playerTwoBoard;

    private GameState gameState = GameState.PENDING;
    private String gameName;
    private String creator;
    private BattleshipsServer server;

    private BattleshipsUserData[] gameQueue = new
            BattleshipsUserData[MAX_PLAYER_COUNT];
    private int playersInQueue = 0;
    private int activePlayerIndex;


    private static final String yourBoardHeading = "       YOUR BOARD";
    private static final String enemyBoardHeading = "      ENEMY BOARD";
    private static final String turnSeparator =
            "   _ _ _ _ _ _ _ _ _   " + System.lineSeparator() +
                    "*| _ _ Next Turn _ _ |*" + System.lineSeparator();

    public BattleshipsGame(String gameName, String creator,
                           BattleshipsServer server) {
        this.gameName = gameName;
        this.creator = creator;
        this.server = server;
    }

    public BattleshipsGame(SavedGame savedGame) {
        this.gameName = savedGame.getGameName();
        this.creator = savedGame.getCreator().toString();
        this.playerOneBoard = savedGame.getCreatorBoard();
        this.playerTwoBoard = savedGame.getOtherBoard();
        this.playerOne = savedGame.getCreator();
        playerOne.setCurrentGame(this);
    }

    public synchronized void join(BattleshipsUserData player) throws
            GameSessionFullException {
        if (playerTwo != null) {
            throw new GameSessionFullException();
        }

        if (playerOne == null) {
            playerOne = player;
            if (playerOneBoard == null) {
                playerOneBoard = new Board();
            }
        } else {
            playerTwo = player;
            if (playerTwoBoard == null) {
                playerTwoBoard = new Board();
            }
        }
        player.setCurrentGame(this);

        if (playerTwo != null) {
            gameState = GameState.AWAITING_START;
        }
    }

    @Override
    public String toString() {
        return gameName;
    }

    public String getCreator() {
        return creator;
    }

    public GameState getGameState() {
        return gameState;
    }

    public synchronized Collection<BattleshipsUserData> getPlayers() {
        return Arrays.asList(playerOne, playerTwo);
    }

    public synchronized int getPlayerCount() {
        if (playerOne == null) {
            return 0;
        } else if (playerTwo == null) {
            return 1;
        } else {
            return 2;
        }
    }

    public synchronized Board getPlayerBoard(BattleshipsUserData player) {
        Board board;
        if (player.equals(playerOne)) {
            board = playerOneBoard;
        } else if (player.equals(playerTwo)) {
            board = playerTwoBoard;
        } else {
            throw new PlayerNotInGameException();
        }

        return board;
    }

    public synchronized int getPlayersInQueue() {
        return playersInQueue;
    }

    public synchronized void enterGameQueue(BattleshipsUserData player) {
        if (gameQueue[MAX_PLAYER_COUNT - 1] != null) {
            throw new GameQueueFullException();
        }

        gameQueue[playersInQueue++] = player;
        player.setCurrentState(ConnectionState.AWAITING_GAME_START);

        if (gameQueue[MAX_PLAYER_COUNT - 1] == null) {
            player.getWriter().println("Waiting for other player to type <start>");
        } else {
            activePlayerIndex = 0;
            gameQueue[activePlayerIndex].setCurrentState(
                    ConnectionState.YOUR_TURN);
            gameQueue[activePlayerIndex + 1].setCurrentState(
                    ConnectionState.NOT_YOUR_TURN);
            gameState = GameState.IN_PROGRESS;
            displayBoards();
        }
    }

    private synchronized void displayBoards() {
        playerOne.getWriter().println(turnSeparator);
        displayBoard(true, playerOneBoard, playerOne);
        displayBoard(false, playerTwoBoard, playerOne);

        playerTwo.getWriter().println(turnSeparator);
        displayBoard(true, playerTwoBoard, playerTwo);
        displayBoard(false, playerOneBoard, playerTwo);
    }

    private synchronized void displayBoard(boolean yourBoard,
                                           Board board,
                                           BattleshipsUserData player) {
        if (yourBoard) {
            player.getWriter().println(yourBoardHeading);
        } else {
            player.getWriter().println(enemyBoardHeading);
        }

        player.getWriter().println(board.toString(yourBoard));
    }

    public synchronized void attackPlayer(BattleshipsUserData attackedPlayer,
                                          String attackPosition) {
        Board board = getPlayerBoard(attackedPlayer);
        boolean hit = board.attack(attackPosition);

        if (board.allShipsAreSunk()) {
            gameOver(attackedPlayer);
            return;
        }

        displayBoards();
        displayHitMessages(attackedPlayer, hit, attackPosition);
        switchPlayerTurns();
    }

    private synchronized void displayHitMessages(BattleshipsUserData attacked,
                                                 boolean hit,
                                                 String attackPosition) {
        BattleshipsUserData attacker = getOtherPlayer(attacked);
        if (hit) {
            attacker.getWriter().println(
                    "Hit enemy ship at: " + attackPosition);
            attacked.getWriter().println(
                    "Got hit by enemy at: " + attackPosition);
        } else {
            attacker.getWriter().println(
                    "Fired at: " + attackPosition + " and missed");
            attacked.getWriter().println(
                    "Enemy fired at: " + attackPosition + " and missed");
        }
        attacker.getWriter().println();
        attacked.getWriter().println();
    }

    private synchronized void switchPlayerTurns() {
        int inactivePlayerIndex = activePlayerIndex;
        activePlayerIndex = 1 - activePlayerIndex;

        gameQueue[inactivePlayerIndex].setCurrentState(
                ConnectionState.NOT_YOUR_TURN);
        gameQueue[activePlayerIndex].setCurrentState(
                ConnectionState.YOUR_TURN);
    }

    private synchronized void gameOver(BattleshipsUserData loser) {
        BattleshipsUserData winner = getOtherPlayer(loser);

        System.out.println(winner + " has won his match!!!");

        winner.gameOver(true);
        loser.gameOver(false);

        gameState = GameState.GAME_OVER;
        server.removeGame(gameName);
    }

    public synchronized BattleshipsUserData getOtherPlayer(
            BattleshipsUserData ourPlayer) {
        if (playerOne.equals(ourPlayer)) {
            return playerTwo;
        } else {
            return playerOne;
        }
    }
}