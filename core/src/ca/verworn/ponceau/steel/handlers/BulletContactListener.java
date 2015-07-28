package ca.verworn.ponceau.steel.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.List;

/**
 * Listens for contacts between fixtures. For now just logs the the body user data of the fixtures that collide.
 */
public class BulletContactListener implements ContactListener {

    private final List<Body> mDestroyedBodies;

    public enum BodyContactType {
        BULLET, WALL, PLAYER;

        public final short maskBit;

        BodyContactType() {
            maskBit = (short) (1 << ordinal());
        }

    }

    public BulletContactListener(List<Body> destroyedBodies) {
        mDestroyedBodies = destroyedBodies;
    }

    @Override
    public void beginContact(Contact contact) {
        BodyContactType contactTypeA = (BodyContactType) contact.getFixtureA().getBody().getUserData();
        BodyContactType contactTypeB = (BodyContactType) contact.getFixtureB().getBody().getUserData();

        if (contactTypeA == BodyContactType.BULLET && contactTypeB == BodyContactType.WALL) {
            processBulletHitsWall(contact.getFixtureA().getBody());
        } else if (contactTypeB == BodyContactType.BULLET && contactTypeA == BodyContactType.WALL) {
            processBulletHitsWall(contact.getFixtureB().getBody());
        } else {
            Gdx.app.log("Contact", String.format("A=%s   B=%s",
                    contact.getFixtureA().getBody().getUserData(),
                    contact.getFixtureB().getBody().getUserData()));
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
}
