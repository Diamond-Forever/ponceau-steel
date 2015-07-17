package ca.verworn.ponceau.steel.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ca.verworn.ponceau.steel.util.MathUtil;

import static ca.verworn.ponceau.steel.handlers.Box2DHelper.PPM;

/**
 * @author Evan Verworn <evan@verworn.ca>
 */
public class Player extends Sprite {

    public Vector2 velocity = new Vector2(0, 0);
    public Vector2 direction = new Vector2(0, 0);
    private final float MAX_SPEED = 30f;
    private final float ACCELERATION = 0.9f;
    private final Body body;
    private final float radius;

    public Player(Sprite sprite, World world) {
        super(sprite);
        radius = getWidth() / 2 / PPM;

        // Box2D Definitions
        BodyDef def = new BodyDef();
        def.type = BodyType.DynamicBody;
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        shape.setPosition(new Vector2((getX() + (getWidth() / 2)) / PPM, (getY() + (getHeight() / 2)) / PPM));
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;

        // Box2D Init
        body = world.createBody(def);
        body.createFixture(fdef);
        body.setLinearDamping(15f);
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
                direction.set(direction.x, 0);
                return true;
            case Keys.A:
                direction.set(0, direction.y);
                return true;
            case Keys.S:
                direction.set(direction.x, 0);
                return true;
            case Keys.D:
                direction.set(0, direction.y);
                return true;
        }
        return false;
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                direction.set(direction.x, 1);
                return true;
            case Keys.A:
                direction.set(-1, direction.y);
                return true;
            case Keys.S:
                direction.set(direction.x, -1);
                return true;
            case Keys.D:
                direction.set(1, direction.y);
                return true;
        }
        return false;
    }

    public boolean touchDown(World world, Vector2 worldClickPosition) {
        Vector2 origin = body.getPosition().cpy().add(radius, radius);
        new Bullet(world, origin, MathUtil.getNormalDirectionVector(origin, worldClickPosition));
        return true;
    }

    private void update(float delta) {
        // Force = Mass * Acceleration
        velocity.set(direction);
        body.applyLinearImpulse(velocity.scl(body.getMass() * ACCELERATION), body.getWorldCenter(), true);
        // update sprite texture to physics sim
        setPosition(body.getPosition().x * PPM, body.getPosition().y * PPM);
    }
}
