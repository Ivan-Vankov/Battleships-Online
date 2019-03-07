package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

// TODO: Add disconnect command
public abstract class Command {
    protected BattleshipsUserData user;
    protected BattleshipsServer server;

    public Command(BattleshipsUserData user, BattleshipsServer server) {
        this.user = user;
        this.server = server;
    }

    /**
     * @return true if the execution was successful
     */
    public abstract boolean execute(String parameters);

    /**
     * @return What state the connection should be
     * if the command was executed successfully
     */
    public abstract ConnectionState getStateAfterSuccessfulCompletion();
}
