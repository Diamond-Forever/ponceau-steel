package ca.verworn.ponceau.steel.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static ca.verworn.ponceau.steel.handlers.Box2DHelper.MPP;

/**
 * Provides optimized camera actions, and at its heart is an OrthographicCamera.
 */
public class PonceauCamera extends OrthographicCamera {

    /**
     * Converts screen coordinates into world coordinates.
     */
    public Vector2 unproject(int screenX, int screenY) {
        float screenHeight = Gdx.graphics.getHeight();

        Vector2 worldPosition = new Vector2(screenX, screenY);
        worldPosition.x = (2f * screenX) / Gdx.graphics.getWidth() - 1f;
        worldPosition.y = (2f * (screenHeight - screenY - 1f)) / screenHeight - 1f;
        project(worldPosition, invProjectionView);

        return worldPosition.scl(MPP);
    }

    /**
     * Projects a 2D vector on the matrix. Will likely not handle rotation, simplified for speed. See {@link
     * com.badlogic.gdx.graphics.Camera#unproject(Vector3, float, float, float, float) Camera.unproject()} for details.
     */
    public static void project(Vector2 vector, Matrix4 matrix) {
        vector.set(
                vector.x * matrix.val[Matrix4.M00] + matrix.val[Matrix4.M03],
                vector.y * matrix.val[Matrix4.M11] + matrix.val[Matrix4.M13]
        );
    }
}
