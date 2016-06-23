package com.costyuk.leprechaun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public interface FallingItem {
	Rectangle getRectangle();
	void setRectangle(Rectangle rectangle);
	Texture getTexture();
	void playSound();
}
