package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

public class ListPlayersCommand extends Command {
    public ListPlayersCommand(BattleshipsUserData user,
                              BattleshipsServer server) {
        super(user, server);
    }

    @Override

    public boolean execute(String parameters) {
        if (!parameters.isEmpty()) {
            String errorMessage = "Command <list-player> does " +
                    "not take parameters";
            System.err.println(errorMessage);
            user.getWriter().println(errorMessage);
            return false;
        }

        listPlayers();
        System.out.println("Successfully listed players to " +
                "user <" + user + ">");
        return true;
    }

    private void listPlayers() {
        int i = 0;
        for (var player : user.getCurrentGame().getPlayers()) {
            user.getWriter().println(i++ + ") " + player +
                    ", state: " + player.getCurrentState());
        }
    }

    @Override
    public ConnectionState getStateAfterSuccessfulCompletion() {
        return user.getCurrentState();
    }
}
