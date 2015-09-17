package ca.verworn.ponceau.steel.entities;

/**
 * Acts as a wrapper for the {@link EntityType} WALL so that we can pass a unified object type (Entity) to the bodies
 * user data to make decisions on collisions. RHYME MODE ACTIVATED.
 */
public class Wall implements Entity {
    @Override
    public EntityType getType() {
        return EntityType.WALL;
    }
}
