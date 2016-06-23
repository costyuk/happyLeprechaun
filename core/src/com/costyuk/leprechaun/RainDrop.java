package com.costyuk.leprechaun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class RainDrop implements FallingItem{
	private Rectangle rectangle;
	private static Texture rainDropImage = new Texture(Gdx.files.internal("droplet.png"));
	private static Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
	
	@Override
	public Rectangle getRectangle() {
		return rectangle;
	}
	
	@Override
	public void setRectangle(Rectangle rectangle){
		this.rectangle = rectangle;
	}

	@Override
	public Texture getTexture() {
		return rainDropImage;
	}
	public static void dispose(){
		rainDropImage.dispose();
		dropSound.dispose();
	}

	@Override
	public void playSound() {
		dropSound.play();
		
	}
}
