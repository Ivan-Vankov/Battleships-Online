package bg.sofia.uni.fmi.mjt.battleships.exceptions;

public class NoSuchPendingGameException extends RuntimeException {
    public NoSuchPendingGameException() {
        super();
    }

    @Override
    public String getMessage() {
        return "No such pending game exists";
    }
}
