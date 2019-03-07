package bg.sofia.uni.fmi.mjt.battleships.game;

import bg.sofia.uni.fmi.mjt.battleships.exceptions.InvalidColumnException;
import bg.sofia.uni.fmi.mjt.battleships.exceptions.InvalidRowException;
import bg.sofia.uni.fmi.mjt.battleships.exceptions.InvalidShipPositionsException;

import java.util.*;

//                      __    __    __
//                     |==|  |==|  |==|
//                   __|__|__|__|__|__|_
//                __|___________________|___
//             __|__[]__[]__[]__[]__[]__[]__|___
//            |............................o.../
//            \.............................../
//      _,~')_,~')_,~')_,~')_,~')_,~')_,~')_,~')/,~')_
public class Ship {
    public static class Position {
        private int x;
        private int y;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Position(int x, int y) throws InvalidRowException,
                InvalidColumnException {
            if (x < 1 || x > Board.BOARD_HEIGHT) {
                throw new InvalidRowException();
            }
            if (y < 1 || y > Board.BOARD_WIDTH) {
                throw new InvalidColumnException();
            }
            this.x = x;
            this.y = y;
        }

        public Position(String position) throws InvalidRowException,
                InvalidColumnException, NumberFormatException {
            position = position.trim();
            x = Board.rowToIndex(position.charAt(0));
            y = Integer.parseInt(position.substring(1));
            if (y < 1 || y > Board.BOARD_WIDTH) {
                throw new InvalidColumnException();
            }
        }

        public static boolean isValidPosition(String position) {
            position = position.trim();
            try {
                int x = Board.rowToIndex(position.charAt(0));
                int y = Integer.parseInt(position.substring(1));
                return isValidPosition(x, y);
            } catch (RuntimeException e) {
                return false;
            }
        }

        public static boolean isValidPosition(int x, int y) {
            return x >= 1 && x <= Board.BOARD_HEIGHT &&
                    y >= 1 && y <= Board.BOARD_WIDTH;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Position)) {
                return false;
            }
            Position other = (Position) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(x) / 2 + Integer.hashCode(y) / 2;
        }
    }

    private Set<Position> hits;
    private Position startPosition;
    private Position endPosition;

    public Ship(Position startPosition, Position endPosition) {
        if (startPosition.x != endPosition.x &&
                startPosition.y != endPosition.y) {
            throw new InvalidShipPositionsException();
        }

        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.hits = new HashSet<>();
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public boolean collidesWith(Ship other) {
        Ship thisNormalized = normalized(this);
        Ship otherNormalized = normalized(other);
        if (isHorizontal()) {
            if (otherNormalized.isHorizontal() &&
                    !(otherNormalized.startPosition.x != thisNormalized.startPosition.x ||
                            otherNormalized.startPosition.y > thisNormalized.endPosition.y ||
                            otherNormalized.endPosition.y < thisNormalized.startPosition.y)) {
                return true;
            } else {
                return otherNormalized.isVertical() &&
                        otherNormalized.startPosition.y >= thisNormalized.startPosition.y &&
                        otherNormalized.startPosition.y <= thisNormalized.endPosition.y &&
                        otherNormalized.startPosition.x <= thisNormalized.startPosition.x &&
                        otherNormalized.endPosition.x >= thisNormalized.startPosition.x;
            }
        } else {
            if (otherNormalized.isHorizontal() &&
                    otherNormalized.startPosition.x >= thisNormalized.startPosition.x &&
                    otherNormalized.startPosition.x <= thisNormalized.endPosition.x &&
                    otherNormalized.startPosition.y <= thisNormalized.startPosition.y &&
                    otherNormalized.endPosition.y >= thisNormalized.startPosition.y) {
                return true;
            } else {
                return otherNormalized.isVertical() &&
                        !(otherNormalized.startPosition.y != thisNormalized.startPosition.y ||
                                otherNormalized.startPosition.x > thisNormalized.endPosition.x ||
                                otherNormalized.endPosition.x < thisNormalized.startPosition.x);
            }
        }
    }

    public boolean hit(Position attackPosition) {
        Ship normalized = normalized(this);
        if (isHorizontal()) {
            if (attackPosition.getX() == startPosition.getX() &&
                    attackPosition.getY() >= normalized.startPosition.getY() &&
                    attackPosition.getY() <= normalized.endPosition.getY()) {
                hits.add(attackPosition);
                return true;
            }
        } else {
            if (attackPosition.getY() == startPosition.getY() &&
                    attackPosition.getX() >= normalized.startPosition.getX() &&
                    attackPosition.getX() <= normalized.endPosition.getX()) {
                hits.add(attackPosition);
                return true;
            }
        }
        return false;
    }

    /**
     * @return A normalized ship, as in the start position is
     * lexicographically lower than the end position
     */
    public static Ship normalized(Ship ship) {
        if ((ship.isHorizontal() && ship.startPosition.y > ship.endPosition.y) ||
                (ship.isVertical() && ship.startPosition.x > ship.endPosition.x)) {
            return new Ship(ship.endPosition, ship.startPosition);
        } else {
            return ship;
        }
    }

    public Ship normalize() {
        Ship norm = normalized(this);
        startPosition = norm.startPosition;
        endPosition = norm.endPosition;
        return this;
    }

    public boolean isHorizontal() {
        return startPosition.x == endPosition.x;
    }

    public boolean isVertical() {
        return startPosition.y == endPosition.y;
    }

    public boolean isSunk() {
        return getLength() == hits.size();
    }

    public Collection<Ship.Position> getPositions() {
        Ship thisNormalized = normalized(this);
        List<Position> positions = new ArrayList<>(getLength());

        if (isVertical()) {
            int y = thisNormalized.startPosition.getY();
            for (int x = thisNormalized.startPosition.getX();
                 x <= thisNormalized.endPosition.getX(); ++x) {
                positions.add(new Position(x, y));
            }
        } else {
            int x = thisNormalized.startPosition.getX();
            for (int y = thisNormalized.startPosition.getY();
                 y <= thisNormalized.endPosition.getY(); ++y) {
                positions.add(new Position(x, y));
            }
        }

        return positions;
    }

    public int getLength() {
        if (isHorizontal()) {
            return Math.abs(startPosition.getY() - endPosition.getY()) + 1;
        } else {
            return Math.abs(startPosition.getX() - endPosition.getX()) + 1;
        }
    }
}
