package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.game.BattleshipsGame;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

public class CreateGameCommand extends Command {
    public CreateGameCommand(BattleshipsUserData user, BattleshipsServer server) {
        super(user, server);
    }

    @Override
    public boolean execute(String gameName) {
        if (gameName.isEmpty()) {
            System.err.println("No game name given");
            user.getWriter().println("Please give a game name");
            return false;
        } else {
            return createGame(gameName);
        }
    }

    private boolean createGame(String gameName) {
        if (server.hasGame(gameName)) {
            String errorMessage = "Game name <" + gameName + "> taken";
            System.err.println(errorMessage);
            user.getWriter().println(errorMessage);
            return false;
        }

        BattleshipsGame game = new BattleshipsGame(
                gameName, user.toString(), server);
        server.addGame(game);
        game.join(user);

        System.out.println("Game <" + gameName + "> created");
        user.getWriter().println(
                "Game <" + gameName + "> created, players 1/2");
        return true;
    }

    @Override
    public ConnectionState getStateAfterSuccessfulCompletion() {
        return ConnectionState.IN_LOBBY;
    }
}
