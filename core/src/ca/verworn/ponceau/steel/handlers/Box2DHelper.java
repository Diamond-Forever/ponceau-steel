
package ca.verworn.ponceau.steel.handlers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Iterator;

import ca.verworn.ponceau.steel.entities.Entity.EntityType;
import ca.verworn.ponceau.steel.entities.Wall;

/**
 * @author Evan Verworn <evan@verworn.ca>
 */
public class Box2DHelper {
    /**
     * Pixels per Meter.
     */
    public static final float PPM = 100f;

    /**
     * Meters per pixel.
     */
    public static final float MPP = 1 / PPM;

    /**
     * Parse the map for blocking tiles.
     * Look at each layer of the map to see if it has a "blocking" property, if
     * so then iterate through and create static bodies for all tiles that also
     * have the "blocking" parameter.
     * 
     * I don't know if it is a good idea to require the "blocking" property on 
     * the layer <b>and</b> the tile, but I figure I might want to disable layers
     * in the future. Meh, we'll see.
     * @param map TiledMap to look at
     * @param world Box2D world to add the bodies to
     */
    public static void parseMapBodies (TiledMap map, World world) {
        MapLayers layers = map.getLayers();
        MapProperties properties = map.getProperties();
        System.out.println(properties.get("Tile Size"));
        Integer pixWidth = properties.get("tilewidth", Integer.class),
                pixHeight = properties.get("tileheight", Integer.class);
        for(MapLayer layer : layers) {
            Iterator<String> i = layer.getProperties().getKeys();
            while(i.hasNext()) {
                String k = i.next();
                System.out.print(k + "(" + layer.getProperties().get(k) + "), ");
            }System.out.println("is layer blocking? " + layer.getProperties().containsKey("blocking"));
            if(layer.getProperties().containsKey("blocking")) {
                // blindly assuming that this is a tile layer
                TiledMapTileLayer tiles = (TiledMapTileLayer) layer;
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                PolygonShape shape = new PolygonShape();
                FixtureDef fdef = new FixtureDef();
                System.out.println(tiles.getWidth() + ", " + tiles.getHeight());
                for(int y = 0; y < tiles.getHeight(); y++ ) {
                    for(int x = 0; x < tiles.getWidth(); x++ ) {
                        Cell cell = tiles.getCell(x, y);
                        if(cell == null) continue;
                        TiledMapTile tile = cell.getTile();
                        shape.setAsBox(pixWidth / 2 / PPM, pixHeight / 2 / PPM);
                        bdef.position.set((x * pixWidth + 8) / PPM, (y * pixHeight + 8) / PPM);

                        fdef.shape = shape;
                        fdef.filter.categoryBits = EntityType.WALL.maskBit;
                        fdef.filter.maskBits = (short) (EntityType.PLAYER.maskBit | EntityType.BULLET.maskBit);

                        Body body = world.createBody(bdef);
                        body.createFixture(fdef);
                        body.setUserData(new Wall());
                    }
                }
            }
        }
    }
}
