package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

public class StartGameCommand extends Command {
    public StartGameCommand(BattleshipsUserData user, BattleshipsServer server) {
        super(user, server);
    }

    @Override
    public boolean execute(String parameters) {
        if (!parameters.isEmpty()) {
            String errorMessage = "Command <start> doesn't take any parameters";
            System.err.println(errorMessage);
            user.getWriter().println(errorMessage);
            return false;
        }

        user.getCurrentGame().enterGameQueue(user);
        return true;
    }

    @Override
    public ConnectionState getStateAfterSuccessfulCompletion() {
        if (user.getCurrentState() == ConnectionState.IN_LOBBY) {
            return ConnectionState.AWAITING_GAME_START;
        } else {
            return user.getCurrentState();
        }
    }
}
