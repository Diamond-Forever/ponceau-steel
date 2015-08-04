package ca.verworn.ponceau.steel.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
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

    public Vector2 velocity = new Vector2(0, 0);
    public Vector2 direction = new Vector2(0, 0);
    private final float MAX_SPEED = 30f;
    private final float ACCELERATION = 0.9f;
    private final Body body;
    private final float radius;
    private Vector2 origin;

    public static Player create(World world) {
        // We define the body first because the player needs a reference to it, even though a lot of the body
        // definition stuff requires access to the players size.
        BodyDef def = new BodyDef();
        def.type = BodyType.DynamicBody;
        Body body = world.createBody(def);

        Player player = new Player(new Sprite(new Texture("player.png")), body);

        CircleShape shape = new CircleShape();
        shape.setRadius(player.radius);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = EntityType.PLAYER.maskBit;
        fdef.filter.maskBits = (short) (EntityType.WALL.maskBit | EntityType.BULLET.maskBit | EntityType.ENEMY.maskBit);

        body.setUserData(player);
        body.createFixture(fdef);
        body.setLinearDamping(15f);

        return player;
    }

    public Player(Sprite sprite, Body body) {
        super(sprite);
        radius = getWidth() / 2 / PPM;
        this.body = body;
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    public void draw(ShapeRenderer renderer, PonceauCamera camera) {
        Vector3 n = camera.project(new Vector3(getX() + radius * PPM, getY() + radius * PPM, 0f));
        renderer.begin(ShapeType.Filled);
        renderer.setColor(0.9f, 0.1f, 0.1f, 0.0f);
        renderer.circle(n.x, n.y, 10f);
        renderer.end();
    }

    public void update(Vector2 mousePosition) {
        // Goal: Point the character towards the mouse.
        float crossProduct = body.getPosition().cpy()
                .add(MathUtils.cos(body.getAngle()), MathUtils.sin(body.getAngle()))
                .crs(mousePosition);
        if (crossProduct > 0) {
            body.setAngularVelocity(1.5f);
        } else if (crossProduct < 0) {
            body.setAngularVelocity(-1.5f);
        } else {
            Gdx.app.log("", "nailed it");
        }
        //        Gdx.app.log("", String.format("%f %f -> %f %f", body.getPosition().x, body.getPosition().x,
        // mousePosition.y,
        //                mousePosition.y));

        // Force = Mass * Acceleration
        velocity.set(direction);
        body.applyLinearImpulse(velocity.scl(body.getMass() * ACCELERATION), body.getWorldCenter(), true);
        // update sprite texture to physics sim
        setPosition((body.getPosition().x - radius) * PPM, (body.getPosition().y - radius) * PPM);
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
                direction.add(0f, -1f);
                return true;
            case Keys.A:
                direction.add(1f, 0f);
                return true;
            case Keys.S:
                direction.add(0f, 1f);
                return true;
            case Keys.D:
                direction.add(-1f, 0f);
                return true;
        }
        return false;
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                direction.add(0f, 1f);
                return true;
            case Keys.A:
                direction.add(-1f, 0f);
                return true;
            case Keys.S:
                direction.add(0f, -1f);
                return true;
            case Keys.D:
                direction.add(1f, 0f);
                return true;
        }
        return false;
    }

    public boolean touchDown(World world, Vector2 mousePosition) {
        Vector2 origin = body.getPosition();
        Bullet.create(world, origin, MathUtil.getNormalDirectionVector(origin, mousePosition));
        return true;
    }

    private double norm(double v) {
        if (v < 0) {
            return v + MathUtils.PI2;
        } else if (v > MathUtils.PI2) {
            return v - MathUtils.PI2;
        } else {
            return v;
        }
    }
}
