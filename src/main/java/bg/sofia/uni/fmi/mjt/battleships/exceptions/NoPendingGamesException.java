package bg.sofia.uni.fmi.mjt.battleships.exceptions;

public class NoPendingGamesException extends RuntimeException {
    public NoPendingGamesException() {
        super();
    }

    @Override
    public String getMessage() {
        return "There are no pending games";
    }
}
