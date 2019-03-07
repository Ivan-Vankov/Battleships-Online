package bg.sofia.uni.fmi.mjt.battleships.exceptions;

public class GameNameTakenException extends RuntimeException {
    public GameNameTakenException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Game name already taken";
    }
}
