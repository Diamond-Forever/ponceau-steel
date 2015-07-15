package ca.verworn.ponceau.steel;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ca.verworn.ponceau.steel.scenes.MapScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * @author Evan
 */
public class PonceauSteel extends Game {

    public SpriteBatch batch;
    public ShapeRenderer renderer;
    public BitmapFont font;
    public World world;
    
    public PonceauSteel () {
      world = new World(new Vector2(0,0), true);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        font = new BitmapFont();
        setScreen(new MapScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
        font.dispose();
    }
}
