package ca.verworn.ponceau.steel.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Listens for contacts between fixtures. For now just logs the the body user data of the fixtures that collide.
 */
public class BulletContactListener implements ContactListener {

    public enum BodyContactType {
        BULLET, WALL, PLAYER;

        public final short maskBit;

        BodyContactType() {
            maskBit = (short) (1 << ordinal());
        }
    }

    @Override
    public void beginContact(Contact contact) {
        Gdx.app.log("Contact", String.format("A=%s   B=%s",
                contact.getFixtureA().getBody().getUserData(),
                contact.getFixtureB().getBody().getUserData()));
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
}
