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

import static ca.verworn.ponceau.steel.handlers.Box2DHelper.PPM;

/**
 * Create a bullet to cause harm to your enemies. Currently bullets just fall on the ground and get in the way, are you
 * putting gunpowder in these??
 */
public class Bullet {
    // Really this should live outside of Bullet, as it should be cleaned up when the application is closing.
    private static Sprite mBulletSprite;

    private final Body mBody;
    private final Vector2 mDirection;

    public Bullet(World world, Vector2 origin, Vector2 direction) {
        if (mBulletSprite == null) {
            mBulletSprite = new Sprite(new Texture("bullet.png"));
        }

        mDirection = direction.cpy();

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(mBulletSprite.getWidth() / 2 / PPM);
        circleShape.setPosition(origin.cpy().add(direction.cpy().scl(1f)));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        // bullets probably shouldn't slow you down
        fixtureDef.density = 0.1f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        mBody = world.createBody(bodyDef);
        mBody.createFixture(fixtureDef);

        // add "air" friction
        mBody.setLinearDamping(5f);
        mBody.setAngularDamping(5f);
    }

    public void draw(Batch batch) {
        mBulletSprite.draw(batch);
    }
}
