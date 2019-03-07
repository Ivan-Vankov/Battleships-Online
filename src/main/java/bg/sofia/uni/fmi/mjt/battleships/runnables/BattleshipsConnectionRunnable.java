package bg.sofia.uni.fmi.mjt.battleships.runnables;

import bg.sofia.uni.fmi.mjt.battleships.commands.Command;
import bg.sofia.uni.fmi.mjt.battleships.commands.factories.*;
import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Modeled as a nondeterministic finite state automaton
 */
public class BattleshipsConnectionRunnable implements Runnable {
    private BattleshipsUserData user;
    private Map<ConnectionState, CommandFactory> commandFactories =
            new HashMap<>();

    public BattleshipsConnectionRunnable(BattleshipsUserData user,
                                         BattleshipsServer server) {
        this.user = user;

        commandFactories.put(ConnectionState.REGULAR, new
                RegularCommandFactory(user, server));
        commandFactories.put(ConnectionState.IN_LOBBY, new
                LobbyCommandFactory(user, server));
        commandFactories.put(ConnectionState.AWAITING_GAME_START, new
                AwaitingGameStartCommandFactory(user, server));
        commandFactories.put(ConnectionState.YOUR_TURN, new
                GameCommandFactory(user, server));
        commandFactories.put(ConnectionState.NOT_YOUR_TURN, new
                GameCommandFactory(user, server));
    }

    @Override
    public void run() {
        while (true) {
            try {
                String commandLine = user.getReader().readLine();

                if (commandLine == null) {
                    return;
                }
                if (commandLine.isEmpty()) {
                    continue;
                }

                System.out.println("Command received: " + commandLine);

                parseCommand(commandLine);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void parseCommand(String commandLine) {
        String[] commandLineSplit = commandLine.trim().split("\\s+", 2);
        String command = commandLineSplit[0];
        String commandParameters = commandLineSplit.length != 1 ?
                commandLineSplit[1] : "";

        executeCommand(command, commandParameters);
    }

    private void executeCommand(String commandName, String parameters) {
        CommandFactory currentCommandFactory = commandFactories
                .get(user.getCurrentState());
        if (!currentCommandFactory.hasCommand(commandName)) {
            System.err.println("Unknown command");
            user.getWriter().println("Unknown command");
            return;
        }

        Command command = currentCommandFactory.getCommand(commandName);

        boolean successfulCompletion = command.execute(parameters);
        if (successfulCompletion) {
            user.setCurrentState(command.getStateAfterSuccessfulCompletion());
        }
    }
}
