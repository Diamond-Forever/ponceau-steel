package ca.verworn.ponceau.steel.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ca.verworn.ponceau.steel.graphics.PonceauCamera;
import ca.verworn.ponceau.steel.util.MathUtil;

import static ca.verworn.ponceau.steel.handlers.Box2DHelper.PPM;

/**
 * @author Evan Verworn <evan@verworn.ca>
 */
public class Player extends Sprite implements Entity {

    private static final float ACCELERATION = 0.9f;

    private final Vector2 mVelocity = new Vector2(0, 0);
    private final Vector2 mDirection = new Vector2(0, 0);
    private final Body mBody;
    private final float mRadius;

    public static Player create(World world) {
        // We define the body first because the player needs a reference to it, even though a lot of the body
        // definition stuff requires access to the players size.
        BodyDef def = new BodyDef();
        def.type = BodyType.DynamicBody;
        Body body = world.createBody(def);

        Player player = new Player(new Sprite(new Texture("player.png")), body);

        CircleShape shape = new CircleShape();
        shape.setRadius(player.mRadius);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = EntityType.PLAYER.maskBit;
        fdef.filter.maskBits = (short) (EntityType.WALL.maskBit | EntityType.BULLET.maskBit | EntityType.ENEMY.maskBit);

        body.setUserData(player);
        body.createFixture(fdef);
        body.setLinearDamping(15f);

        return player;
    }

    private Player(Sprite sprite, Body body) {
        super(sprite);
        mRadius = getWidth() / 2 / PPM;
        mBody = body;
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    public void draw(ShapeRenderer renderer, PonceauCamera camera) {
        Vector3 n = camera.project(new Vector3(getX() + mRadius * PPM, getY() + mRadius * PPM, 0f));
        renderer.begin(ShapeType.Filled);
        renderer.setColor(0.9f, 0.1f, 0.1f, 0.0f);
        renderer.circle(n.x, n.y, 10f);
        renderer.end();
    }

    public void update(Vector2 mousePosition) {
        // Goal: Point the character towards the mouse.

        switch (MathUtil.rotationDirection(mBody.getAngle(), mBody.getPosition(), mousePosition)) {
            case CLOCKWISE:
                mBody.setAngularVelocity(3.5f);
                break;
            case COUNTER_CLOCKWISE:
                mBody.setAngularVelocity(-3.5f);
                break;
            default:
                mBody.setAngularVelocity(0.0f);
        }

        // Force = Mass * Acceleration
        mVelocity.set(mDirection);
        mBody.applyLinearImpulse(mVelocity.scl(mBody.getMass() * ACCELERATION), mBody.getWorldCenter(), true);
        // update sprite texture to physics sim
        setPosition((mBody.getPosition().x - mRadius) * PPM, (mBody.getPosition().y - mRadius) * PPM);
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
                mDirection.add(0f, -1f);
                return true;
            case Keys.A:
                mDirection.add(1f, 0f);
                return true;
            case Keys.S:
                mDirection.add(0f, 1f);
                return true;
            case Keys.D:
                mDirection.add(-1f, 0f);
                return true;
        }
        return false;
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                mDirection.add(0f, 1f);
                return true;
            case Keys.A:
                mDirection.add(-1f, 0f);
                return true;
            case Keys.S:
                mDirection.add(0f, -1f);
                return true;
            case Keys.D:
                mDirection.add(1f, 0f);
                return true;
        }
        return false;
    }

    public boolean touchDown(World world) {
        Bullet.create(world, mBody.getPosition(), mBody.getAngle());
        return true;
    }
}
