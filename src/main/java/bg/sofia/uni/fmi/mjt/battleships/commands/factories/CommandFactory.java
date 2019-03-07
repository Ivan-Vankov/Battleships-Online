package bg.sofia.uni.fmi.mjt.battleships.commands.factories;

import bg.sofia.uni.fmi.mjt.battleships.commands.Command;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandFactory {
    protected Map<String, Command> commands = new HashMap<>();

    public boolean hasCommand(String commandName) {
        return commands.containsKey(commandName);
    }

    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }
}
