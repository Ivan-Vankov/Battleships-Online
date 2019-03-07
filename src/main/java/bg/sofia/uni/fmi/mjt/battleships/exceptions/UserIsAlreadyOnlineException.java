package bg.sofia.uni.fmi.mjt.battleships.exceptions;

public class UserIsAlreadyOnlineException extends RuntimeException {
    public UserIsAlreadyOnlineException() {
        super();
    }

    @Override
    public String getMessage() {
        return "That user is already online";
    }
}
