package bg.sofia.uni.fmi.mjt.battleships.user;

import bg.sofia.uni.fmi.mjt.battleships.enums.ConnectionState;
import bg.sofia.uni.fmi.mjt.battleships.game.BattleshipsGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class BattleshipsUserData {
    private String userName;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private BattleshipsGame currentGame = null;
    private ConnectionState currentState = ConnectionState.REGULAR;

    public BattleshipsUserData(String userName, Socket socket,
                               BufferedReader reader,
                               PrintWriter writer) {
        this.userName = userName;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public BattleshipsGame getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(BattleshipsGame currentGame) {
        this.currentGame = currentGame;
    }

    public ConnectionState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ConnectionState currentState) {
        this.currentState = currentState;
    }

    public boolean isOnline() {
        return !socket.isClosed();
    }

    public void close() {
        try {
            socket.close();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gameOver(boolean victory) {
        if (victory) {
            victory();
        } else {
            defeat();
        }
    }

    // TODO: Play sweet victory
    private void victory() {
        writer.println("Sweet victory, yea!");
        setCurrentState(ConnectionState.REGULAR);
        setCurrentGame(null);
    }

    // TODO: Play sound of silence
    private void defeat() {
        writer.println("Defeat ;(");
        setCurrentState(ConnectionState.REGULAR);
        setCurrentGame(null);
    }

    @Override
    public String toString() {
        return userName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BattleshipsUserData)) {
            return false;
        }
        BattleshipsUserData other = (BattleshipsUserData) obj;
        return userName.equals(other.userName);
    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }
}
