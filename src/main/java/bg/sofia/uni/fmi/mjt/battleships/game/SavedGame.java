package bg.sofia.uni.fmi.mjt.battleships.game;

import bg.sofia.uni.fmi.mjt.battleships.user.BattleshipsUserData;

public class SavedGame {
    private Board creatorBoard;
    private Board otherBoard;
    private String gameName;
    private BattleshipsUserData creator;

    public SavedGame(Board creatorBoard, Board otherBoard,
                     String gameName, BattleshipsUserData creator) throws
            CloneNotSupportedException {
        this.creatorBoard = (Board) creatorBoard.clone();
        this.otherBoard = (Board) otherBoard.clone();
        this.gameName = gameName;
        this.creator = creator;
    }

    public Board getCreatorBoard() {
        return creatorBoard;
    }

    public Board getOtherBoard() {
        return otherBoard;
    }

    public String getGameName() {
        return gameName;
    }

    public BattleshipsUserData getCreator() {
        return creator;
    }
}
