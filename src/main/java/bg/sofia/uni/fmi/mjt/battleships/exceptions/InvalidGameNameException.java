package bg.sofia.uni.fmi.mjt.battleships.exceptions;

public class InvalidGameNameException extends RuntimeException {
    public InvalidGameNameException() {
        super();
    }

    @Override
    public String getMessage() {
        return "A game can't have \";\" in its name";
    }
}
