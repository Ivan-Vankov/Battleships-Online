package bg.sofia.uni.fmi.mjt.battleships.game;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ShipTest {

    @Test
    public void testShipIsVertical() {
        Ship verticalShip = new Ship(new Ship.Position(1, 5),
                new Ship.Position(4, 5));
        Ship horizontalShip = new Ship(new Ship.Position(2, 3),
                new Ship.Position(2, 8));
        assertTrue("Didn't detect vertical ship",
                verticalShip.isVertical());
        assertFalse("Detected horizontal ship as vertical",
                horizontalShip.isVertical());
    }

    @Test
    public void testShipIsHorizontal() {
        Ship verticalShip = new Ship(new Ship.Position(1, 5),
                new Ship.Position(4, 5));
        Ship horizontalShip = new Ship(new Ship.Position(2, 3),
                new Ship.Position(2, 8));
        assertTrue("Didn't detect horizontal ship",
                horizontalShip.isHorizontal());
        assertFalse("Detected vertical ship as horizontal",
                verticalShip.isHorizontal());
    }

    @Test
    public void testShipVerticalAndHorizontalShipCollideProperly() {
        Ship verticalShip = new Ship(new Ship.Position(1, 5),
                new Ship.Position(4, 5));
        Ship horizontalShip = new Ship(new Ship.Position(2, 3),
                new Ship.Position(2, 8));
        assertTrue("Didn't detect ship collision", verticalShip.collidesWith(horizontalShip));
        assertTrue("Didn't detect ship collision", horizontalShip.collidesWith(verticalShip));
    }

    @Test
    public void testShipTwoVerticalShipsCollision() {
        Ship verticalShip1 = new Ship(new Ship.Position(3, 2),
                new Ship.Position(8, 2));
        Ship verticalShip2 = new Ship(new Ship.Position(5, 2),
                new Ship.Position(10, 2));
        assertTrue("Didn't detect collision", verticalShip1.collidesWith(verticalShip2));
        Ship verticalShip3 = new Ship(new Ship.Position(9, 2),
                new Ship.Position(10, 2));
        assertFalse("Incorrectly detected collision", verticalShip1.collidesWith(verticalShip3));
    }

    @Test
    public void testHitXAndYInside() {
        Ship verticalShip = new Ship(new Ship.Position(1, 5),
                new Ship.Position(4, 5));
        Ship horizontalShip = new Ship(new Ship.Position(2, 3),
                new Ship.Position(2, 8));
        Ship.Position attackPosition = new Ship.Position(2, 5);
        assertTrue("Didn't hit vertical ship",
                verticalShip.hit(attackPosition));
        assertTrue("Didn't hit horizontal ship",
                horizontalShip.hit(attackPosition));
    }

    @Test
    public void testHitXAndYOutside() {
        Ship verticalShip = new Ship(new Ship.Position(1, 5),
                new Ship.Position(4, 5));
        Ship horizontalShip = new Ship(new Ship.Position(2, 3),
                new Ship.Position(2, 8));
        Ship.Position attackPosition = new Ship.Position(1, 1);
        assertFalse("Hit vertical ship", verticalShip.hit(attackPosition));
        assertFalse("Hit horizontal ship", horizontalShip.hit(attackPosition));
    }

    @Test
    public void testGetPositions() {
        Ship.Position verticalStart = new Ship.Position(1, 5);
        Ship.Position verticalEnd = new Ship.Position(2, 5);
        Ship verticalShip = new Ship(verticalStart, verticalEnd);

        Ship.Position horizontalStart = new Ship.Position(2, 3);
        Ship.Position horizontalEnd = new Ship.Position(2, 4);
        Ship horizontalShip = new Ship(horizontalStart, horizontalEnd);
        assertEquals("Improper vertical ship positions",
                Arrays.asList(verticalStart, verticalEnd),
                verticalShip.getPositions());
        assertEquals("Improper horizontal ship positions",
                Arrays.asList(horizontalStart, horizontalEnd),
                horizontalShip.getPositions());
    }

    @Test

    public void testGetLength() {
        Ship verticalShip = new Ship(new Ship.Position(1, 5),
                new Ship.Position(4, 5));
        Ship horizontalShip = new Ship(new Ship.Position(2, 3),
                new Ship.Position(2, 8));
        int x = 1;
        int y = 1;
        assertEquals("Improper vertical ship length", 4, verticalShip.getLength());
        assertEquals("Improper horizontal ship length", 6, horizontalShip.getLength());
    }

    @Test
    public void testIsSunkWithRedundantHits() {
        Ship ship = new Ship(new Ship.Position(1, 5),
                new Ship.Position(2, 5));
        Ship.Position attackPosition1 = new Ship.Position(1, 5);
        Ship.Position attackPosition2 = new Ship.Position(2, 5);
        ship.hit(attackPosition1);
        ship.hit(attackPosition1);
        ship.hit(attackPosition2);
        assertTrue("Didn't sink ship", ship.isSunk());
    }
}
