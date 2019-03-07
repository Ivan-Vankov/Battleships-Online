package bg.sofia.uni.fmi.mjt.battleships.enums;

public enum GameState {
    PENDING("pending"),
    AWAITING_START("awaiting start"),
    IN_PROGRESS("in progress"),
    GAME_OVER("game over");

    String status;

    GameState(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}