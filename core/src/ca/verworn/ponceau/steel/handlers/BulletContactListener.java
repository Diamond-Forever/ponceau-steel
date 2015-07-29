package ca.verworn.ponceau.steel.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.Set;

import ca.verworn.ponceau.steel.entities.Enemy;
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

        if (entityTypeA == EntityType.BULLET) {
            bulletCollision(contact.getFixtureA().getBody(), contact.getFixtureB().getBody(), entityTypeB);
        } else if (entityTypeB == EntityType.BULLET) {
            bulletCollision(contact.getFixtureB().getBody(), contact.getFixtureA().getBody(), entityTypeA);
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

    private void bulletCollision(Body bulletBody, Body otherBody, EntityType otherEntityType) {
        Gdx.app.log("Contact", "bullet hits " + otherEntityType);

        if (otherEntityType == EntityType.WALL) {
            mDestroyedBodies.add(bulletBody);
        } else if (otherEntityType == EntityType.ENEMY) {
            Enemy enemy = (Enemy) otherBody.getUserData();
            enemy.gotHit();
            if (enemy.isDead()) {
                mDestroyedBodies.add(otherBody);
            }
            mDestroyedBodies.add(bulletBody);
        }
    }

    private static EntityType getEntityType(Fixture fixture) {
        return ((Entity) fixture.getBody().getUserData()).getType();
    }
}
