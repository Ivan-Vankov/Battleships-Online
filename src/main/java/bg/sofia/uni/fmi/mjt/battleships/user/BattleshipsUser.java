package bg.sofia.uni.fmi.mjt.battleships.user;

import bg.sofia.uni.fmi.mjt.battleships.exceptions.InvalidUserNameException;
import bg.sofia.uni.fmi.mjt.battleships.exceptions.UserIsAlreadyOnlineException;
import bg.sofia.uni.fmi.mjt.battleships.runnables.BattleshipsServerResponseListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class BattleshipsUser {
    private Socket socket;
    private PrintWriter writer;
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public BattleshipsUser(String userName) throws IOException {
        socket = new Socket(HOST, PORT);

        writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println(userName);

        var reader = new BufferedReader(new
                InputStreamReader(socket.getInputStream()));
        String serverResponse = reader.readLine();

        if (serverResponse.equals("User <" + userName +
                "> is already online")) {
            throw new UserIsAlreadyOnlineException();
        } else {
            System.out.println(serverResponse);
            new Thread(new
                    BattleshipsServerResponseListener(reader)).start();
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please add a username");
            return;
        }

        try {
            String[] userNameArray = Arrays.copyOfRange(
                    args, 0, args.length);
            String userName = String.join(" ",
                    userNameArray);


            if (userName.contains(";")) {
                throw new InvalidUserNameException();
            }
            new BattleshipsUser(String.join(" ",
                    userNameArray)).start();
        } catch (IOException e) {
            System.err.println("Cannot connect to the server. " +
                    "Make sure that the server is started");
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                if (socket.isClosed()) {
                    return;
                }

                String command = scanner.nextLine();
                if (command == null) {
                    return;
                }

                writer.println(command);
            }
        }
    }
}
