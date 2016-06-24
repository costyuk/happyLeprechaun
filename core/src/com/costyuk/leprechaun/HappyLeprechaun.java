package com.costyuk.leprechaun;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class HappyLeprechaun extends ApplicationAdapter {
	private final int SCREEN_HEIGHT;
	private final int SCREEN_WIDTH;
	private Texture bucketImage;
	private Texture leprechaunImage;

	private Music rainMusic;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Rectangle bucket;
	private Array<FallingItem> fallingItems;
	private long lastDropTime;

	public HappyLeprechaun(int height, int width) {
		SCREEN_HEIGHT = height;
		SCREEN_WIDTH = width;
	}
	@Override
	public void create() {

		leprechaunImage = new Texture(Gdx.files.internal("leprechaun.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		rainMusic.setLooping(true);
		rainMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		batch = new SpriteBatch();

		bucket = new Rectangle();
		bucket.x = SCREEN_WIDTH / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		fallingItems = new Array<FallingItem>();
		spawnFallingItem();
	}

	private void spawnFallingItem() {
		Rectangle rectangle = new Rectangle();
		rectangle.x = MathUtils.random(0, SCREEN_WIDTH - 64);
		rectangle.y = SCREEN_HEIGHT;
		rectangle.width = 64;
		rectangle.height = 64;
		FallingItem item = getFailingItemRandomly();
		item.setRectangle(rectangle);
		fallingItems.add(item);
		lastDropTime = TimeUtils.nanoTime();
	}

	private FallingItem getFailingItemRandomly() {
		if (MathUtils.randomBoolean()) {
			return new Coin();
		} else {
			return new RainDrop();
		}
	}

	@Override
	public void render() {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(leprechaunImage, SCREEN_WIDTH - 221, 0);
		batch.draw(bucketImage, bucket.x, bucket.y);
		for (FallingItem item : fallingItems) {
			batch.draw(item.getTexture(), item.getRectangle().x, item.getRectangle().y);
		}
		batch.end();

		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// make sure the bucket stays within the screen bounds
		if (bucket.x < 0)
			bucket.x = 0;
		if (bucket.x > SCREEN_WIDTH - 64)
			bucket.x = SCREEN_WIDTH - 64;

		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnFallingItem();

		Iterator<FallingItem> iter = fallingItems.iterator();
		while (iter.hasNext()) {
			FallingItem item = iter.next();
			Rectangle rectangle = item.getRectangle();
			rectangle.y -= 200 * Gdx.graphics.getDeltaTime();
			if (rectangle.y + 64 < 0)
				iter.remove();
			if (rectangle.overlaps(bucket)) {
				item.playSound();
				iter.remove();
			}
		}
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		Coin.dispose();
		RainDrop.dispose();
		bucketImage.dispose();
		leprechaunImage.dispose();
		rainMusic.dispose();
		batch.dispose();
	}
}
