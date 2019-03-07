package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.game.BattleshipsGame;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

import java.util.Collection;

public class ListGamesCommand extends Command {
    public ListGamesCommand(BattleshipsUserData user, BattleshipsServer server) {
        super(user, server);
    }

    @Override
    public boolean execute(String parameters) {
        if (!parameters.isEmpty()) {
            String errorMessage = "Command <list-games> " +
                    "doesn't take any parameters";
            System.err.println(errorMessage);
            user.getWriter().println(errorMessage);
            return false;
        }

        Collection<BattleshipsGame> games = server.getAllGames();
        if (games.isEmpty()) {
            String errorMessage = "There are no currently active games";
            System.err.println(errorMessage);
            user.getWriter().println(errorMessage);
            return false;
        }

        listGames(games);
        return true;
    }

    // TODO: Fix list games:
    // | NAME    | CREATOR  | STATUS  | PLAYERS |
    // |---------+---------+---------+---------|
    // | my game | Moist Boy| pending | 1/2     |
    private void listGames(Collection<BattleshipsGame> games) {
        int maxNameLength = "NAME".length();
        int maxCreatorLength = "CREATOR".length();
        int maxStatusLength = "STATUS".length();
        for (var game : games) {
            int currNameLength = game.toString().length();
            if (currNameLength > maxNameLength) {
                maxNameLength = currNameLength;
            }

            int currCreatorLength = game.getCreator().length();
            if (currCreatorLength > maxCreatorLength) {
                maxCreatorLength = currCreatorLength;
            }

            int currStatusLength = game.getGameState()
                    .toString().length();
            if (currStatusLength > maxStatusLength) {
                maxStatusLength = currStatusLength;
            }
        }

        printHeader(maxNameLength, maxCreatorLength, maxStatusLength);
        printGames(server.getAllGames(),
                maxNameLength, maxCreatorLength, maxStatusLength);
    }

    private void printHeader(int maxNameLength,
                             int maxCreatorLength,
                             int maxStatusLength) {
        StringBuilder result = new StringBuilder();
        result.append("| NAME ");
        for (int i = 0; i < maxNameLength - "NAME".length(); ++i) {
            result.append(" ");
        }
        result.append("| CREATOR");
        for (int i = 0; i < maxCreatorLength -
                "CREATOR".length() + 1; ++i) {
            result.append(" ");
        }
        result.append("| STATUS ");
        for (int i = 0; i < maxStatusLength - "STATUS".length(); ++i) {
            result.append(" ");
        }
        result.append("| PLAYERS |").append(System.lineSeparator());

        result.append("|");
        for (int i = 0; i < maxNameLength + 2; ++i) {
            result.append("-");
        }
        result.append("+");
        for (int i = 0; i < maxCreatorLength + 2; ++i) {
            result.append("-");
        }
        result.append("+");
        for (int i = 0; i < maxStatusLength + 2; ++i) {
            result.append("-");
        }
        result.append("+---------|").append(System.lineSeparator());

        user.getWriter().print(result);
    }

    private void printGames(Collection<BattleshipsGame> games,
                            int maxNameLength,
                            int maxCreatorLength,
                            int maxStatusLength) {
        StringBuilder gameLine = new StringBuilder();

        for (var game : games) {
            gameLine.append("| ");
            String currName = game.toString();

            gameLine.append(currName);
            for (int i = currName.length(); i < maxNameLength; ++i) {
                gameLine.append(" ");
            }
            gameLine.append(" | ");


            String currCreator = game.getCreator();
            gameLine.append(currCreator);
            for (int i = currCreator.length(); i < maxCreatorLength; ++i) {
                gameLine.append(" ");
            }
            gameLine.append(" | ");

            String currStatus = game.getGameState().toString();
            gameLine.append(currStatus);
            for (int i = currStatus.length(); i < maxStatusLength; ++i) {
                gameLine.append(" ");
            }
            gameLine.append(" | ");

            String playerCount = game.getPlayerCount() + "/2     |";
            gameLine.append(playerCount);

            user.getWriter().println(gameLine);
            gameLine.setLength(0);
        }
    }

    @Override

    public ConnectionState getStateAfterSuccessfulCompletion() {
        return ConnectionState.REGULAR;
    }
}
