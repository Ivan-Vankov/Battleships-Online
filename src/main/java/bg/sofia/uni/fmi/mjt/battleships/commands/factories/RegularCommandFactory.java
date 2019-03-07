package bg.sofia.uni.fmi.mjt.battleships.commands.factories;

import bg.sofia.uni.fmi.mjt.battleships.commands.*;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

public class RegularCommandFactory extends CommandFactory {
    public RegularCommandFactory(BattleshipsUserData user, BattleshipsServer server) {
        commands.put("create-game", new CreateGameCommand(user, server));
        commands.put("load-game", new LoadGameCommand(user, server));
        commands.put("join-game", new JoinGameCommand(user, server));
        commands.put("saved-games", new SavedGamesCommand(user, server));
        commands.put("list-games", new ListGamesCommand(user, server));
    }
}
