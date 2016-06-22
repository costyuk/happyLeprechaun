package com.costyuk.leprechaun.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.costyuk.leprechaun.HappyLeprechaun;

public class HtmlLauncher extends GwtApplication {

	@Override
	public ApplicationListener createApplicationListener() {
		return new HappyLeprechaun();
	}

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(800, 480);
	}
}