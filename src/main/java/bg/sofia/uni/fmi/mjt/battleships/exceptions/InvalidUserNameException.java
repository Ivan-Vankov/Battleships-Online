package bg.sofia.uni.fmi.mjt.battleships.exceptions;

public class InvalidUserNameException extends RuntimeException {
    public InvalidUserNameException() {
        super();
    }

    @Override
    public String getMessage() {
        return "A user name can't contain \";\"";
    }
}
