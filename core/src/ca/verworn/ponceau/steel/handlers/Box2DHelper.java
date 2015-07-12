
package ca.verworn.ponceau.steel.handlers;

/**
 *
 * @author Evan Verworn <evan@verworn.ca>
 */
public class Box2DHelper {
    /**
     * Pixels per Meter
     */
    public static final float PPM = 100f;
    
    public static float ToBox(float f) {
        return f / PPM;
    }
}
