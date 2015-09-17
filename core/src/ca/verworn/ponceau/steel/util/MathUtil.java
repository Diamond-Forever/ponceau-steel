package ca.verworn.ponceau.steel.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.math.MathUtils.PI2;

public class MathUtil {

    public enum Direction {CLOCKWISE, COUNTER_CLOCKWISE, NORMAL}

    /**
     * 1 degree = 0.0175 radians.
     */
    private static final float CLOSE_ENOUGH_RADIANS = 3 * 0.0175f;
    private static final float CLOSE_ENOUGH_RADIANS_UPPER_BOUNDS = MathUtils.PI2 - CLOSE_ENOUGH_RADIANS;

    /**
     * Determines the direction the character should rotate to point towards the mouse. This is determined using polar
     * angles.
     */
    public static Direction rotationDirection(float playerAngle, Vector2 playerOrigin, Vector2 mousePosition) {
        float angleToMouse = angleBetween(playerOrigin, mousePosition);
        float degreeOfChange = normalizeAngle(angleToMouse - playerAngle);
        if (degreeOfChange < MathUtils.PI) {
            if (degreeOfChange < CLOSE_ENOUGH_RADIANS) {
                return Direction.NORMAL;
            } else {
                return Direction.CLOCKWISE;
            }
        } else {
            if (degreeOfChange > CLOSE_ENOUGH_RADIANS_UPPER_BOUNDS) {
                return Direction.NORMAL;
            } else {
                return Direction.COUNTER_CLOCKWISE;
            }
        }
    }

    public static float angleBetween(Vector2 origin, Vector2 point) {
        return normalizeAngle((float) Math.atan2(point.y - origin.y, point.x - origin.x));
    }

    private static float normalizeAngle(float angle) {
        if (angle < 0.0f) {
            // In java the expression (-5 % 2) returns -1 instead of 1.
            return (angle % PI2) + PI2;
        } else {
            return angle % PI2;
        }
    }
}
