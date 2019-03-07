package bg.sofia.uni.fmi.mjt.battleships.exceptions;

public class PlayerNotInGameException extends RuntimeException {
    public PlayerNotInGameException() {
        super();
    }

    @Override
    public String getMessage() {
        return "That player is not in the game";
    }
}
