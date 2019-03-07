package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

public class SavedGamesCommand extends Command {
    public SavedGamesCommand(BattleshipsUserData user, BattleshipsServer server) {
        super(user, server);
    }

    @Override
    public boolean execute(String parameters) {
        if (!parameters.isEmpty()) {
            System.err.println("Command saved-games " +
                    "doesn't take any parameters");
            user.getWriter().println("Command saved-games " +
                    "doesn't take any parameters");
            return false;
        } else {
            printSavedGames();
            return true;
        }
    }

    private void printSavedGames() {
        var savedGames = server.getSavedGames(user.toString()).values();
        if (savedGames.isEmpty()) {
            user.getWriter().println("You have no saved games");
        } else {
            user.getWriter().println("Your saved games:");
            int i = 0;
            for (var savedGame : savedGames) {
                user.getWriter().println(i++ + ": " + savedGame.getGameName());
            }
        }
    }

    @Override

    public ConnectionState getStateAfterSuccessfulCompletion() {
        return ConnectionState.REGULAR;
    }
}
