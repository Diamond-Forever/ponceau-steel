package ca.verworn.ponceau.steel.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ca.verworn.ponceau.steel.handlers.BulletContactListener.BodyContactType;

import static ca.verworn.ponceau.steel.handlers.Box2DHelper.PPM;

/**
 * Create a bullet to cause harm to your enemies. Currently bullets just fall on the ground and get in the way, are you
 * putting gunpowder in these??
 */
public class Bullet {
    private static final float INITIAL_VELOCITY_METERS_PER_SECOND = 9.0f;

    // Really this should live outside of Bullet, as it should be cleaned up when the application is closing.
    private static Sprite mBulletSprite;

    private final Body mBody;

    public Bullet(World world, Vector2 origin, Vector2 direction) {
        if (mBulletSprite == null) {
            mBulletSprite = new Sprite(new Texture("bullet.png"));
        }

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(mBulletSprite.getWidth() / 2 / PPM);
        circleShape.setPosition(origin.cpy().add(direction.cpy().scl(0.38f)));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        // bullets probably shouldn't slow you down
        fixtureDef.density = 0.1f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        mBody = world.createBody(bodyDef);
        mBody.createFixture(fixtureDef);
        mBody.setUserData(BodyContactType.BULLET);

        // add "air" friction
        mBody.setLinearDamping(0.2f);
        mBody.setAngularDamping(0.2f);

        mBody.applyLinearImpulse(direction.cpy().scl(mBody.getMass() * INITIAL_VELOCITY_METERS_PER_SECOND),
                mBody.getWorldCenter(), true);
    }

    public void draw(Batch batch) {
        mBulletSprite.draw(batch);
    }

    private void update(float timeDelta) {

    }
}
