package bg.sofia.uni.fmi.mjt.battleships.commands.factories;

import bg.sofia.uni.fmi.mjt.battleships.commands.AttackCommand;
import bg.sofia.uni.fmi.mjt.battleships.commands.SaveGameCommand;
import bg.sofia.uni.fmi.mjt.battleships.game.Ship;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;
import bg.sofia.uni.fmi.mjt.battleships.commands.Command;

public class GameCommandFactory extends CommandFactory {
    private BattleshipsUserData user;
    private BattleshipsServer server;

    public GameCommandFactory(BattleshipsUserData user, BattleshipsServer server) {
        this.user = user;
        this.server = server;

        // TODO: add a chat command where you can type 1 line
        //  and 1 line only to your opponent each turn
        //  (so that the player feels that it counts
        //   and so that the other player does not get spammed)
        commands.put("save-game", new SaveGameCommand(user, server));
    }

    @Override
    public boolean hasCommand(String command) {
        if (commands.containsKey(command)) {
            return true;
        } else {
            // Check if the command can be interpreted as an attack position
            return Ship.Position.isValidPosition(command);
        }
    }

    @Override
    public Command getCommand(String commandName) {
        if (commands.containsKey(commandName)) {
            return commands.get(commandName);
        } else {
            return new AttackCommand(user, server, commandName);
        }

    }
}
