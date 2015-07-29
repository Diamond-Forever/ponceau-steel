package ca.verworn.ponceau.steel.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.Set;

import ca.verworn.ponceau.steel.entities.Entity;
import ca.verworn.ponceau.steel.entities.Entity.EntityType;

/**
 * Listens for contacts between fixtures. For now just logs the the body user data of the fixtures that collide.
 */
public class BulletContactListener implements ContactListener {

    private final Set<Body> mDestroyedBodies;

    public BulletContactListener(Set<Body> destroyedBodies) {
        mDestroyedBodies = destroyedBodies;
    }

    @Override
    public void beginContact(Contact contact) {
        EntityType entityTypeA = getEntityType(contact.getFixtureA());
        EntityType entityTypeB = getEntityType(contact.getFixtureB());

        if (entityTypeA == EntityType.BULLET && entityTypeB == EntityType.WALL) {
            processBulletHitsWall(contact.getFixtureA().getBody());
        } else if (entityTypeB == EntityType.BULLET && entityTypeA == EntityType.WALL) {
            processBulletHitsWall(contact.getFixtureB().getBody());
        } else {
            Gdx.app.log("Contact", String.format("A=%s   B=%s", entityTypeA, entityTypeB));
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Intentionally empty.
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Intentionally empty.
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Intentionally empty.
    }

    private void processBulletHitsWall(Body bulletBody) {
        Gdx.app.log("Contact", "bullet hits wall!");
        mDestroyedBodies.add(bulletBody);
    }

    private static EntityType getEntityType(Fixture fixture) {
        return ((Entity) fixture.getBody().getUserData()).getType();
    }
}
