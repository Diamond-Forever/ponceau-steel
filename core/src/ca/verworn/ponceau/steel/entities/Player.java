package ca.verworn.ponceau.steel.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
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
public class Player extends Sprite implements Entity {

    public Vector2 velocity = new Vector2(0, 0);
    public Vector2 direction = new Vector2(0, 0);
    private final float MAX_SPEED = 30f;
    private final float ACCELERATION = 0.9f;
    private final Body body;
    private final float radius;

    public static Player create(World world) {
        // We define the body first because the player needs a reference to it, even though a lot of the body
        // definition stuff requires access to the players size.
        BodyDef def = new BodyDef();
        def.type = BodyType.DynamicBody;
        Body body = world.createBody(def);

        Player player = new Player(new Sprite(new Texture("player.png")), body);

        CircleShape shape = new CircleShape();
        shape.setRadius(player.radius);
        shape.setPosition(new Vector2(
                (player.getX() + (player.getWidth() / 2)) / PPM,
                (player.getY() + (player.getHeight() / 2)) / PPM));

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
        Bullet.create(world, origin, MathUtil.getNormalDirectionVector(origin, worldClickPosition));
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
