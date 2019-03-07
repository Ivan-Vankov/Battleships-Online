package bg.sofia.uni.fmi.mjt.battleships.game;

import bg.sofia.uni.fmi.mjt.battleships.exceptions.InvalidColumnException;
import bg.sofia.uni.fmi.mjt.battleships.exceptions.InvalidRowException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShipPositionTest {

    @Test
    public void testShipPositionStringConstructorValidInput() {
        String position = "B4";
        Ship.Position pos = new Ship.Position(position);

        assertEquals("Didn't convert row properly", pos.getX(), 2);
        assertEquals("Didn't convert column properly", pos.getY(), 4);
    }

    @Test(expected = InvalidRowException.class)

    public void testShipPositionStringConstructorInvalidRow() {
        new Ship.Position("Z4");
    }

    @Test(expected = NumberFormatException.class)
    public void testShipPositionStringConstructorColumnNotANumber() {
        new Ship.Position("BK");
    }

    @Test(expected = InvalidColumnException.class)
    public void testShipPositionStringConstructorColumnTooLarge() {
        new Ship.Position("B100");
    }

    @Test(expected = InvalidColumnException.class)
    public void testShipPositionStringConstructorNegativeColumn() {
        new Ship.Position("B-100");
    }
}
