package bg.sofia.uni.fmi.mjt.battleships.enums;

public enum ConnectionState {
    REGULAR("regular"),
    IN_LOBBY("in lobby"),
    AWAITING_GAME_START("awaiting game start"),
    YOUR_TURN("in game"),
    NOT_YOUR_TURN("not your turn");

    String state;

    ConnectionState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }
}