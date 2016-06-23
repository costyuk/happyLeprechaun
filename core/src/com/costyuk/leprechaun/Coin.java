package com.costyuk.leprechaun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Coin implements FallingItem{
	private Rectangle rectangle;
	private static Texture coinImage = new Texture(Gdx.files.internal("coin.png"));
	private static Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("coinDrop.mp3"));
	
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
		return coinImage;
	}
	public static void dispose(){
		coinImage.dispose();
		dropSound.dispose();
	}

	@Override
	public void playSound() {
		dropSound.play();
	}
}
