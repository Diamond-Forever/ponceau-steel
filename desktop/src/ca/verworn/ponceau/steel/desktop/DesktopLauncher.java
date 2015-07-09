package ca.verworn.ponceau.steel.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ca.verworn.ponceau.steel.PonceauSteel;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.width = 800;
                config.height = 400;
                config.title = "Ponceau Steel";
		new LwjglApplication(new PonceauSteel(), config);
	}
}
