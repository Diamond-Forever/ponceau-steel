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
 * Create a bullet to cause harm to your enemies. Bullets will not collide with each other, and slow down very slowly.
 */
public class Bullet implements Entity {
    private static final float INITIAL_VELOCITY_METERS_PER_SECOND = 3.0f;

    // Really this should live outside of Bullet, as it should be cleaned up when the application is closing.
    private static Sprite mBulletSprite;

    public static void create(World world, Vector2 origin, Vector2 direction) {
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
        fixtureDef.restitution = 0.1f;
        fixtureDef.filter.categoryBits = EntityType.BULLET.maskBit;
        fixtureDef.filter.maskBits = (short) (EntityType.PLAYER.maskBit | EntityType.WALL.maskBit
                | EntityType.ENEMY.maskBit);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(new Bullet());

        // add "air" friction
        body.setLinearDamping(0.2f);
        body.setAngularDamping(0.2f);

        body.applyLinearImpulse(direction.cpy().scl(body.getMass() * INITIAL_VELOCITY_METERS_PER_SECOND), body
                .getWorldCenter(), true);
    }

    @Override
    public EntityType getType() {
        return EntityType.BULLET;
    }

    public void draw(Batch batch) {
        mBulletSprite.draw(batch);
    }
}
