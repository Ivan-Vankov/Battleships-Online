package bg.sofia.uni.fmi.mjt.battleships.commands.factories;

import bg.sofia.uni.fmi.mjt.battleships.commands.PlayersInLobbyCommand;
import bg.sofia.uni.fmi.mjt.battleships.commands.SavedGamesCommand;
import bg.sofia.uni.fmi.mjt.battleships.commands.StartGameCommand;
import bg.sofia.uni.fmi.mjt.battleships.commands.ListGamesCommand;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

public class LobbyCommandFactory extends CommandFactory {
    public LobbyCommandFactory(BattleshipsUserData user, BattleshipsServer server) {
        commands.put("players-in-lobby", new PlayersInLobbyCommand(user, server));
        commands.put("start", new StartGameCommand(user, server));
        commands.put("saved-games", new SavedGamesCommand(user, server));
        commands.put("list-games", new ListGamesCommand(user, server));
    }
}
