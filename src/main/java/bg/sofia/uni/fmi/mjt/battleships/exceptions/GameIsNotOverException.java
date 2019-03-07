package bg.sofia.uni.fmi.mjt.battleships.exceptions;

public class GameIsNotOverException extends RuntimeException {
    public GameIsNotOverException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Game isn't over";
    }
}
