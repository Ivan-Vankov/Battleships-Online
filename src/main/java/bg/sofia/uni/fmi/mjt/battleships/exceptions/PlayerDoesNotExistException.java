package bg.sofia.uni.fmi.mjt.battleships.exceptions;

public class PlayerDoesNotExistException extends RuntimeException {
    public PlayerDoesNotExistException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Player doesn't exist";
    }
}
