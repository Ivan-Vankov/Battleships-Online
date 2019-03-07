package bg.sofia.uni.fmi.mjt.battleships.commands;

import bg.sofia.uni.fmi.mjt.battleships.game.BattleshipsGame;
import bg.sofia.uni.fmi.mjt.battleships.game.Ship;
import bg.sofia.uni.fmi.mjt.battleships.server.BattleshipsServer;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;
import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;

public class AttackCommand extends Command {
    private String attackPosition;

    public AttackCommand(BattleshipsUserData user,
                         BattleshipsServer server,
                         String attackPosition) {
        super(user, server);
        this.attackPosition = attackPosition;
    }

    @Override
    public boolean execute(String parameters) {
        if (!parameters.isEmpty()) {
            String errorMessage = "Invalid attack command";
            System.err.println(errorMessage);
            user.getWriter().println(errorMessage);
            return false;
        }

        if (user.getCurrentState() == ConnectionState.NOT_YOUR_TURN) {
            if (Ship.Position.isValidPosition(attackPosition)) {
                System.err.println("It is not " + user +
                        "'s turn to attack yet");
                user.getWriter().println("It is not your " +
                        "turn to attack yet");
                return false;
            } else {
                String errorMessage = "Unknown command";
                System.err.println(errorMessage);
                user.getWriter().println(errorMessage);
                return false;
            }
        }

        return attack();
    }

    private boolean attack() {
        try {
            BattleshipsGame game = user.getCurrentGame();
            BattleshipsUserData otherPlayer = game.getOtherPlayer(user);
            game.attackPlayer(otherPlayer, attackPosition);
            return true;
        } catch (IllegalArgumentException e) {
            user.getWriter().println("Please enter a valid attack position.");
            user.getWriter().println("Example: B4");
            return false;
        }
    }

    @Override
    public ConnectionState getStateAfterSuccessfulCompletion() {
        return user.getCurrentState();
    }
}
