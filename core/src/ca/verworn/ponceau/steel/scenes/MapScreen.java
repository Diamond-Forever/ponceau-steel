
package ca.verworn.ponceau.steel.scenes;

import ca.verworn.ponceau.steel.PonceauSteel;
import ca.verworn.ponceau.steel.entities.Player;
import ca.verworn.ponceau.steel.handlers.Box2DHelper;
import static ca.verworn.ponceau.steel.handlers.Box2DHelper.PPM;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This class is an example of loading a map, with a player, 
 * with collision (coming soon).
 * @author Evan Verworn <evan@verworn.ca>
 */
public class MapScreen implements Screen{

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    
    private final PonceauSteel game;
    private Player player;
    private World world;
    
    //BOX2D
    private Box2DDebugRenderer debug;
    private OrthographicCamera debugCam;
    
    public MapScreen(final PonceauSteel game) {
        this.game = game;
    }
        
    @Override
    public void show() {
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/simplemap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        
        world = new World(new Vector2(0,0), true); // box2D
        debug = new Box2DDebugRenderer();
        debugCam = new OrthographicCamera();
        
        player = new Player(new Sprite(new Texture("player.png")), world);
        
        Box2DHelper.parseMapBodies(map, world);
    }

    @Override
    public void render(float delta) {
        // follow the player
        camera.position.set(player.getX() + player.getWidth() / 2f, player.getY() + player.getHeight()/ 2f, 0);
        camera.update();
        
        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // render map
        renderer.setView(camera);
        renderer.render();
        
        // hijack the map's batch to draw the player.
        renderer.getBatch().begin();
            player.draw(renderer.getBatch());
        renderer.getBatch().end();
        
        // update physics
        world.step(delta, 5, 2);
                
        debugCam.position.set(camera.position.scl(1f/PPM));
        debugCam.update();
        debug.render(world, debugCam.combined);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
        
        debugCam.setToOrtho(false, width / PPM, height / PPM);
        debugCam.update();
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        debug.dispose();
        world.dispose();
        map.dispose();
        renderer.dispose();
    }

}
