package bg.sofia.uni.fmi.mjt.battleships.game;

import bg.sofia.uni.fmi.mjt.battleships.enums.Field;
import bg.sofia.uni.fmi.mjt.battleships.exceptions.InvalidRowException;
import bg.sofia.uni.fmi.mjt.battleships.utility.ArrayShuffler;
import bg.sofia.uni.fmi.mjt.battleships.utility.SoundPlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Board implements Cloneable {

    public static final int BOARD_HEIGHT = 10;
    public static final int BOARD_WIDTH = 10;

    private static final int SHIPS_COUNT = 10;
    private static final int[] SHIP_LENGTHS = {
            5, 4, 4, 3, 3, 3, 2, 2, 2, 2
    };

    private static final char[] ROWS = {
            'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J'
    };

    private static final String WATER_SPLASH_SOUND = "src/main/resources/Water Splash Sound.wav";
    private static final String BOMB_HIT_SOUND = "src/main/resources/Bomb Hit Sound.wav";
    private static final String SHIP_SUNK_SOUND = "src/main/resources/Ship Sunk Sound.wav";

    private Field[][] board;
    private Ship[] ships;
    // Used to make displaying ships easier
    private Map<Ship.Position, Object> shipPositions = new HashMap<>();

    public Board() {
        board = new Field[BOARD_HEIGHT][];
        for (int i = 0; i < BOARD_HEIGHT; ++i) {
            board[i] = new Field[BOARD_WIDTH];
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board[i][j] = Field.EMPTY;
            }
        }

        ships = new Ship[SHIPS_COUNT];
        initializeShips();
    }

    private void initializeShips() {
        for (int i = 0; i < SHIPS_COUNT; ++i) {
            Ship.Position startPosition = getRandomStartPosition();
            int shipLength = SHIP_LENGTHS[i] - 1;
            if (!tryAllRotations(startPosition, shipLength, i)) {
                --i;
            }
        }
    }

    public boolean attack(String position) {
        position = position.trim();
        Ship.Position attackPosition = new Ship.Position(position);
        return attack(attackPosition);
    }

    private boolean attack(Ship.Position attackPosition) {
        for (Ship ship : ships) {
            if (ship.hit(attackPosition)) {
                board[attackPosition.getX() - 1][attackPosition.getY() - 1] = Field.HIT_SHIP;
                if (ship.isSunk()) {
                    SoundPlayer.playSound(SHIP_SUNK_SOUND);
                } else {
                    SoundPlayer.playSound(BOMB_HIT_SOUND);
                }
                return true;
            }
        }

        board[attackPosition.getX() - 1][attackPosition.getY() - 1] = Field.HIT_EMPTY;
        SoundPlayer.playSound(WATER_SPLASH_SOUND);
        return false;
    }

    private static Ship.Position getRandomStartPosition() {
        Random rand = new Random();
        int x = rand.nextInt(Board.BOARD_WIDTH) + 1;
        int y = rand.nextInt(Board.BOARD_HEIGHT) + 1;
        return new Ship.Position(x, y);
    }

    /**
     * @param startPosition    current ship starting position
     * @param shipLength       current ship length
     * @param initializedShips ships initialized up to this point
     * @return true if the ship can be constructed in one of the 4 directions
     * false otherwise
     */
    private boolean tryAllRotations(Ship.Position startPosition, int shipLength,
                                    int initializedShips) {
        Ship[] inAllDirections = new Ship[4];

        if (Ship.Position.isValidPosition(
                startPosition.getX(),
                startPosition.getY() + shipLength)) {
            Ship.Position endPositionRight = new Ship.Position(
                    startPosition.getX(),
                    startPosition.getY() + shipLength);

            inAllDirections[0] = new Ship(startPosition, endPositionRight);
        }

        if (Ship.Position.isValidPosition(
                startPosition.getX() + shipLength,
                startPosition.getY())) {
            Ship.Position endPositionDown = new Ship.Position(
                    startPosition.getX() + shipLength,
                    startPosition.getY());
            inAllDirections[1] = new Ship(startPosition, endPositionDown);
        }

        if (Ship.Position.isValidPosition(
                startPosition.getX(),
                startPosition.getY() - shipLength)) {
            Ship.Position endPositionLeft = new Ship.Position(
                    startPosition.getX(),
                    startPosition.getY() - shipLength);
            inAllDirections[2] = new Ship(startPosition, endPositionLeft).normalize();
        }

        if (Ship.Position.isValidPosition(
                startPosition.getX() - shipLength,
                startPosition.getY())) {
            Ship.Position endPositionUp = new Ship.Position(
                    startPosition.getX() - shipLength,
                    startPosition.getY());
            inAllDirections[3] = new Ship(startPosition, endPositionUp).normalize();
        }

        int[] shuffle = {0, 1, 2, 3};
        ArrayShuffler.shuffle(shuffle);

        for (int i : shuffle) {
            if (inAllDirections[i] != null &&
                    addIfDoesNotCollide(inAllDirections[i], initializedShips)) {
                return true;
            }
        }

        return false;
    }

    private boolean addIfDoesNotCollide(Ship ship, int initializedShips) {
        if (hasNoCollisions(ship, initializedShips)) {
            ships[initializedShips] = ship;
            for (var position : ship.getPositions()) {
                shipPositions.put(position, null);
            }
            return true;
        }
        return false;
    }

    private boolean hasNoCollisions(Ship ship, int initializedShips) {
        if (isNotInBoard(ship.getEndPosition()) ||
                isNotInBoard(ship.getStartPosition())) {
            return false;
        }
        for (int i = 0; i < initializedShips; ++i) {
            if (ship.collidesWith(ships[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean isNotInBoard(Ship.Position position) {
        return position.getX() > BOARD_WIDTH ||
                position.getX() < 1 ||
                position.getY() > BOARD_HEIGHT ||
                position.getY() < 1;
    }

    public String toString(boolean showShips) {
        StringBuilder result = new
                StringBuilder(BOARD_HEIGHT * BOARD_WIDTH);
        result.append("  ");
        for (int i = 1; i <= BOARD_WIDTH; ++i) {
            result.append(" ").append(i);
        }
        result.append(System.lineSeparator());

        result.append("  ");
        for (int i = 1; i <= BOARD_WIDTH; ++i) {
            result.append(" _");
        }
        result.append(System.lineSeparator());

        for (int i = 0; i < BOARD_HEIGHT; ++i) {
            result.append(ROWS[i]).append(" ");
            for (int j = 0; j < BOARD_WIDTH; ++j) {
                result.append("|");

                Field currentField = board[i][j];
                if (currentField == Field.HIT_EMPTY ||
                        currentField == Field.HIT_SHIP) {
                    result.append(currentField.getChar());
                } else if (showShips) {
                    Ship.Position currentPosition = new
                            Ship.Position(i + 1, j + 1);
                    if (shipPositions.containsKey(currentPosition)) {
                        result.append(Field.SHIP.getChar());
                    } else {
                        result.append(Field.EMPTY.getChar());
                    }
                } else {
                    result.append(Field.EMPTY.getChar());
                }
            }
            result.append("|").append(System.lineSeparator());
        }
        return result.toString();
    }

    public static int rowToIndex(char row) throws InvalidRowException {
        if (row > ROWS[ROWS.length - 1] || row < ROWS[0]) {
            throw new InvalidRowException();
        }
        return row - ROWS[0] + 1;
    }

    public boolean allShipsAreSunk() {
        for (var ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        Board newBoard = (Board) super.clone();
        newBoard.board = Arrays.copyOf(board, board.length);
        for (int i = 0; i < board.length; ++i) {
            newBoard.board[i] = Arrays.copyOf(board[i], board[i].length);
        }

        newBoard.ships = Arrays.copyOf(ships, ships.length);
        return newBoard;
    }
}
