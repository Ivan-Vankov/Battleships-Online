package bg.sofia.uni.fmi.mjt.battleships.server;

import bg.sofia.uni.fmi.mjt.battleships.enums.GameState;
import bg.sofia.uni.fmi.mjt.battleships.exceptions.*;
import bg.sofia.uni.fmi.mjt.battleships.game.BattleshipsGame;
import bg.sofia.uni.fmi.mjt.battleships.game.SavedGame;
import bg.sofia.uni.fmi.mjt.battleships.runnables.BattleshipsConnectionRunnable;
import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BattleshipsServer {
    // Map from userName to saved games
    private Map<String, Map<String, SavedGame>> savedGames =
            new Hashtable<>();

    // Map from userName to userData
    private Map<String, BattleshipsUserData> users =
            new Hashtable<>();

    // Maps from gameName to game
    private Map<String, BattleshipsGame> pendingGames =
            new Hashtable<>();
    // Field allGames is for O(1) time in the list-games command
    private Map<String, BattleshipsGame> allGames =
            new Hashtable<>();

    private final ExecutorService executor =
            Executors.newCachedThreadPool();

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        new BattleshipsServer().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(HOST, PORT));
            System.out.println("Started new server on host " +
                    HOST + " and port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();

                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

                    String userName = reader.readLine();

                    connectUser(userName, socket, reader);
                } catch (IOException e) {
                    System.err.println("Problem while establishing connection");
                    System.err.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Couldn't create server. " +
                    "Maybe another program is using port " + PORT + ".");
            System.err.println(e.getMessage());
        }
    }

    private synchronized void connectUser(String userName, Socket socket, BufferedReader reader)
            throws IOException {
        var writer = new PrintWriter(socket.getOutputStream(), true);
        if (users.containsKey(userName) && users.get(userName).isOnline()) {
            System.err.println("User <" + userName + "> is already online");
            writer.println("User <" + userName + "> is already online");
            return;
        }

        var userData = new BattleshipsUserData(
                userName, socket, reader, writer);
        users.put(userName, userData);
        savedGames.put(userName, new HashMap<>());
        System.out.println("User " + userName + " connected on port " + PORT);
        writer.println("Connected to server on port " + PORT);

        executor.execute(new BattleshipsConnectionRunnable(userData, this));
    }

    public void addSavedGame(SavedGame gameName, String userName) {
        if (!users.containsKey(userName)) {
            throw new PlayerDoesNotExistException();
        }
        savedGames.get(userName).put(gameName.getGameName(), gameName);
    }

    public Map<String, SavedGame> getSavedGames(String userName) {
        return savedGames.get(userName);
    }

    public synchronized void addGame(BattleshipsGame game) {
        if (hasActiveGame(game.toString())) {
            throw new GameNameTakenException();
        }

        pendingGames.put(game.toString(), game);
        allGames.put(game.toString(), game);
    }

    public synchronized void joinPlayerToGame(BattleshipsUserData user, String gameName) {
        if (!hasPendingGames()) {
            throw new NoPendingGamesException();
        }
        if (!hasPendingGame(gameName)) {
            throw new NoSuchPendingGameException();
        }

        BattleshipsGame game = pendingGames.get(gameName);
        game.join(user);
        pendingGames.remove(game.toString());
    }

    public synchronized boolean hasGame(String gameName) {
        return allGames.containsKey(gameName);
    }

    public synchronized boolean hasPendingGame(String gameName) {
        return pendingGames.containsKey(gameName);
    }

    public synchronized boolean hasActiveGame(String gameName) {
        return hasGame(gameName) && !hasPendingGame(gameName);
    }

    public synchronized boolean hasPendingGames() {
        return pendingGames.size() != 0;
    }

    public synchronized BattleshipsGame getRandomPendingGame() {
        if (!hasPendingGames()) {
            return null;
        } else {
            return pendingGames.values().iterator().next();
        }
    }

    public Collection<BattleshipsGame> getAllGames() {
        return allGames.values();
    }

    public synchronized void removeGame(String gameName) {
        BattleshipsGame gameToRemove = allGames.get(gameName);
        if (gameToRemove == null) {
            throw new GameDoesNotExistException();
        }
        if (gameToRemove.getGameState() != GameState.GAME_OVER) {
            throw new GameIsNotOverException();
        }

        allGames.remove(gameName);
    }
}
