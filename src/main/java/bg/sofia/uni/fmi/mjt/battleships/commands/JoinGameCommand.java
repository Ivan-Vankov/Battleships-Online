package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.exceptions.NoPendingGamesException;
import bg.sofia.uni.fmi.mjt.battleships.exceptions.NoSuchPendingGameException;
import bg.sofia.uni.fmi.mjt.battleships.game.BattleshipsGame;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

public class JoinGameCommand extends Command {
    public JoinGameCommand(BattleshipsUserData user, BattleshipsServer server) {
        super(user, server);
    }

    @Override
    public boolean execute(String gameName) {
        try {
            if (gameName.isEmpty()) {
                return joinRandomGame();
            } else {
                return joinGame(gameName);
            }

        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            user.getWriter().println(e.getMessage());
            return false;
        }
    }

    public synchronized boolean joinRandomGame() {
        if (!server.hasPendingGames()) {
            System.err.println("There are no pending games");
            user.getWriter().println("There are no pending games");
            return false;
        }

        String randomGameName = server.getRandomPendingGame().toString();
        return joinGame(randomGameName);
    }

    public synchronized boolean joinGame(String gameName) {
        try {
            server.joinPlayerToGame(user, gameName);

            System.out.println("User <" + user + "> " +
                    "successfully joined game <" + gameName + ">");
            user.getWriter().println(
                    "Successfully joined game <" + gameName + ">");

            if (user.getCurrentGame().getPlayerCount() == BattleshipsGame.MAX_PLAYER_COUNT) {
                notifyPlayersOfLobbyFill();
            }
            return true;

        } catch (NoPendingGamesException e) {
            System.err.println("There are no pending games");
            user.getWriter().println("There are no pending games");
            return false;

        } catch (NoSuchPendingGameException e) {
            System.err.println("No pending game <" + gameName + "> exists");
            user.getWriter().println("No pending game <" + gameName + "> exists");
            return false;
        }
    }

    private void notifyPlayersOfLobbyFill() {
        for (var player : user.getCurrentGame().getPlayers()) {
            player.getWriter().println("Game is ready to start!" +
                    System.lineSeparator() + "Type <start> to continue");
        }
    }

    @Override
    public ConnectionState getStateAfterSuccessfulCompletion() {
        return ConnectionState.IN_LOBBY;
    }
}
