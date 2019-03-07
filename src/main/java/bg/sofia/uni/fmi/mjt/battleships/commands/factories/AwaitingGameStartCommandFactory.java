package bg.sofia.uni.fmi.mjt.battleships.commands.factories;

import bg.sofia.uni.fmi.mjt.battleships.commands.ListPlayersCommand;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

public class AwaitingGameStartCommandFactory extends CommandFactory {
    public AwaitingGameStartCommandFactory(BattleshipsUserData user,
                                           BattleshipsServer server) {
        commands.put("list-players", new ListPlayersCommand(user, server));
    }
}
