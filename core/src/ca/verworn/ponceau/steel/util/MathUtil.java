package ca.verworn.ponceau.steel.util;

import com.badlogic.gdx.math.Vector2;

public class MathUtil {

    /**
     * Returns the normalized direction vector between two points. Does not modify the input vectors.
     */
    public static Vector2 getNormalDirectionVector(Vector2 origin, Vector2 destination) {
        float distance = origin.dst(destination);
        return new Vector2((destination.x - origin.x) / distance, (destination.y - origin.y) / distance);
    }
}
