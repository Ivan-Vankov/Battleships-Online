package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.game.SavedGame;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

public class SaveGameCommand extends Command {
    public SaveGameCommand(BattleshipsUserData user, BattleshipsServer server) {
        super(user, server);
    }

    @Override
    public boolean execute(String parameters) {
        if (!parameters.isEmpty()) {
            String errorMessage = "Command <save-game> " +
                    "doesn't take any parameters";
            System.err.println(errorMessage);
            user.getWriter().println(errorMessage);
            return false;
        }

        return saveGame();
    }

    private boolean saveGame() {
        try {
            var game = user.getCurrentGame();
            BattleshipsUserData otherPlayer = game.getOtherPlayer(user);

            SavedGame savedGame = new SavedGame(
                    game.getPlayerBoard(user),
                    game.getPlayerBoard(otherPlayer),
                    game.toString(), user);

            server.addSavedGame(savedGame, user.toString());

            return true;
        } catch (CloneNotSupportedException e) {
            String errorMessage = "Couldn't clone boards while saving game";
            System.err.println(errorMessage);
            user.getWriter().println(errorMessage);
            return false;
        }
    }

    @Override

    public ConnectionState getStateAfterSuccessfulCompletion() {
        return user.getCurrentState();
    }
}
