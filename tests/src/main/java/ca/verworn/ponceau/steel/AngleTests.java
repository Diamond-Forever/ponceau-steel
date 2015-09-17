package ca.verworn.ponceau.steel;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import org.junit.Test;

import ca.verworn.ponceau.steel.util.MathUtil;

import static ca.verworn.ponceau.steel.util.MathUtil.Direction.CLOCKWISE;
import static ca.verworn.ponceau.steel.util.MathUtil.Direction.COUNTER_CLOCKWISE;
import static org.junit.Assert.assertEquals;

/**
 * Tests different cases for the MathUtil class which deals with angles.
 */
public class AngleTests {
    private static final double ACCEPTABLE_DELTA = 0.001f;
    private static final Vector2 ORIGIN = Vector2.Zero;
    private static final Vector2 ANGLE_0 = new Vector2(1.0f, 0.0f);
    private static final Vector2 ANGLE_PI_BY_2 = new Vector2(0.0f, 1.0f);
    private static final Vector2 ANGLE_PI = new Vector2(-1.0f, 0.0f);
    private static final Vector2 ANGLE_2PI_BY3 = new Vector2(0.0f, -1.0f);

    @Test
    public void test_angleBetween() throws Exception {
        assertEquals(MathUtil.angleBetween(ORIGIN, ANGLE_0), 0.0f, ACCEPTABLE_DELTA);
        assertEquals(MathUtil.angleBetween(ORIGIN, ANGLE_PI_BY_2), MathUtils.PI / 2.0f, ACCEPTABLE_DELTA);
        assertEquals(MathUtil.angleBetween(ORIGIN, ANGLE_PI), MathUtils.PI, ACCEPTABLE_DELTA);
        assertEquals(MathUtil.angleBetween(ORIGIN, ANGLE_2PI_BY3), MathUtils.PI * 3.0f / 2.0f, ACCEPTABLE_DELTA);
    }

    @Test
    public void test_directionToTurn() {
        assertEquals(MathUtil.rotationDirection(MathUtils.PI / 4.0f, ORIGIN, ANGLE_PI_BY_2), CLOCKWISE);
        assertEquals(MathUtil.rotationDirection(MathUtils.PI / 1.5f, ORIGIN, ANGLE_PI_BY_2), COUNTER_CLOCKWISE);
        assertEquals(MathUtil.rotationDirection(MathUtils.PI / 4.0f, ORIGIN, ANGLE_2PI_BY3), COUNTER_CLOCKWISE);
        assertEquals(MathUtil.rotationDirection(MathUtils.PI / 4.0f, ORIGIN, ANGLE_PI), CLOCKWISE);
    }
}
