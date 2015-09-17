package ca.verworn.ponceau.steel.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import static ca.verworn.ponceau.steel.handlers.Box2DHelper.PPM;

/**
 * Enemy is currently unused, but is intended to spin around in place and get shot. Once it takes enough damage it will
 * disappear (remove itself) from the world.
 */
public class Enemy implements Entity {

    private int mHp = 7;

    /**
     * Creates an enemy at {@param origin} and add it to the world.
     */
    public static void create(World world, Vector2 origin) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(50 / 2 / PPM);
        circleShape.setPosition(origin);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 5000f;
        fixtureDef.filter.categoryBits = EntityType.ENEMY.maskBit;
        fixtureDef.filter.maskBits = (short) (EntityType.BULLET.maskBit | EntityType.PLAYER.maskBit);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(new Enemy());
    }

    @Override
    public EntityType getType() {
        return EntityType.ENEMY;
    }

    public void gotHit() {
        mHp -= 1;
    }

    public boolean isDead() {
        return mHp <= 0;
    }
}
