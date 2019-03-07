package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.game.BattleshipsGame;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

public class PlayersInLobbyCommand extends Command {
    public PlayersInLobbyCommand(BattleshipsUserData user, BattleshipsServer server) {
        super(user, server);
    }

    @Override
    public boolean execute(String parameters) {
        if (!parameters.isEmpty()) {
            System.err.println("Command <players-in-lobby> doesn't take any parameters");
            user.getWriter().println("Command <players-in-lobby> doesn't take any parameters");
            return false;
        }

        String message = "There are " + user.getCurrentGame().getPlayerCount() +
                "/" + BattleshipsGame.MAX_PLAYER_COUNT + " players in the lobby";
        System.out.println(message);
        user.getWriter().println(message);
        return true;
    }

    @Override
    public ConnectionState getStateAfterSuccessfulCompletion() {
        return ConnectionState.IN_LOBBY;
    }
}
