package ca.verworn.ponceau.steel.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Enemy is currently unused, but is intended to spin around in place and get shot. Once it takes enough damage it will
 * disappear (remove itself) from the world.
 */
public class Enemy implements Entity {

    /**
     * Creates an enemy at {@param origin} and add it to the world.
     */
    public static void create(World world, Vector2 origin) {
        // Todo: Intentionally empty for now.
    }

    @Override
    public EntityType getType() {
        return EntityType.ENEMY;
    }
}
