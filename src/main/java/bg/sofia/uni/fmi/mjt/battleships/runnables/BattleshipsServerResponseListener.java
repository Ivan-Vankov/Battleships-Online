package bg.sofia.uni.fmi.mjt.battleships.runnables;

import java.io.BufferedReader;
import java.io.IOException;

public class BattleshipsServerResponseListener implements Runnable {
    BufferedReader reader;

    public BattleshipsServerResponseListener(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String serverResponse = reader.readLine();

                if (serverResponse == null) {
                    return;
                }

                System.out.println(serverResponse);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
