package com.costyuk.leprechaun.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.costyuk.leprechaun.HappyLeprechaun;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	      config.title = "Happy Leprechaun";
	      config.width = 800;
	      config.height = 480;
		new LwjglApplication(new HappyLeprechaun(), config);
	}
}
