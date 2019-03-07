package bg.sofia.uni.fmi.mjt.battleships.enums;

public enum Field {
    EMPTY('_'),
    SHIP('*'),
    HIT_SHIP('X'),
    HIT_EMPTY('O');


    private char character;

    Field(char character) {
        this.character = character;
    }

    public char getChar() {
        return character;
    }
}