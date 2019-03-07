package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.exceptions.GameNameTakenException;
import bg.sofia.uni.fmi.mjt.battleships.game.BattleshipsGame;
import bg.sofia.uni.fmi.mjt.battleships.game.SavedGame;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

public class LoadGameCommand extends Command {
    public LoadGameCommand(BattleshipsUserData user, BattleshipsServer server) {
        super(user, server);
    }

    @Override
    public boolean execute(String gameName) {
        if (gameName.isEmpty()) {
            System.err.println("No game name given");
            user.getWriter().println("Please give a game name");
            return false;
        } else {
            return loadGame(gameName);
        }
    }

    private boolean loadGame(String gameName) {
        var savedGames = server.getSavedGames(user.toString());

        if (!savedGames.containsKey(gameName)) {
            String errorMessage = "No saved game <" + gameName + "> exists";
            System.err.println(errorMessage);
            user.getWriter().println(errorMessage);
            return false;
        }

        SavedGame savedGame = savedGames.get(gameName);
        try {
            var game = new BattleshipsGame(savedGame);
            server.addGame(game);
            savedGames.remove(gameName);
            user.setCurrentGame(game);

            return true;
        } catch (GameNameTakenException e) {
            System.err.println(e.getMessage());
            user.getWriter().println(e.getMessage());
            return false;
        }
    }

    @Override
    public ConnectionState getStateAfterSuccessfulCompletion() {
        return null;
    }
}
