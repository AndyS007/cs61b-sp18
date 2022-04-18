package byog.Core;

import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {

    @Test
    public void move() {
        Position p = new Position(0, 0);
        p.move(World.Movement.UP);
        assertEquals(0, p.x);
        assertEquals(1, p.y);
        p.move(World.Movement.LEFT);
        assertEquals(-1, p.x);

    }
}