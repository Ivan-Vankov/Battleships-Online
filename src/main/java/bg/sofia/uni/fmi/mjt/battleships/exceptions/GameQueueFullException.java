package bg.sofia.uni.fmi.mjt.battleships.exceptions;

public class GameQueueFullException extends RuntimeException {
    public GameQueueFullException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Game queue is full";
    }
}
