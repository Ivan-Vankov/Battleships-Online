package bg.sofia.uni.fmi.mjt.battleships.exceptions;

public class GameDoesNotExistException extends RuntimeException {
    public GameDoesNotExistException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Game doesn't exist";
    }
}