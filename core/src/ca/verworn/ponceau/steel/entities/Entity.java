package ca.verworn.ponceau.steel.entities;

/**
 * Entities will implement this interface to return their "type". The {@link EntityType} is used when collision occurs
 * between two bodies to determine what event should happen. For example a BULLET hitting a WALL will just get removed,
 * where a BULLET hitting an ENEMY will cause the bullet to get removed and the enemy will take damage.
 *
 * The EntityType enum provides a convenient way for us to do collision masking. In Box2D you can set a fixtures
 * collision mask to `BULLET | WALL` to specify that it collides with both bullets and walls. This is achieved by giving
 * each enum a bit. We use the enums ordinal value to bit shift 1 up to 2^16. We can only have 16 types in this scheme,
 * which matches what Box2D supports for masking.
 */
public interface Entity {
    enum EntityType {
        BULLET, WALL, PLAYER, ENEMY;

        public final short maskBit;

        EntityType() {
            maskBit = (short) (1 << ordinal());
        }
    }

    EntityType getType();
}
