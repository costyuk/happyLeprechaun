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
	private Texture bucketImage;
	private Texture leprechaunImage;

	private Music rainMusic;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Rectangle bucket;
	private Array<FallingItem> fallingItems;
	private long lastDropTime;

	@Override
	public void create() {
		// load the images for the droplet and the bucket, 64x64 pixels each
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		leprechaunImage = new Texture(Gdx.files.internal("leprechaun.png"));

		// load the drop sound effect and the rain background "music"

		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start the playback of the background music immediately
		rainMusic.setLooping(true);
		rainMusic.play();

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();

		// create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
		bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
						// the bottom screen edge
		bucket.width = 64;
		bucket.height = 64;

		// create the raindrops array and spawn the first raindrop
		fallingItems = new Array<FallingItem>();
		spawnFallingItem();
	}

	private void spawnFallingItem() {
		Rectangle rectangle = new Rectangle();
		rectangle.x = MathUtils.random(0, 800 - 64);
		rectangle.y = 480;
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

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		batch.draw(leprechaunImage, 800 - 221, 0);
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
		if (bucket.x > 800 - 64)
			bucket.x = 800 - 64;

		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 2000000000)
			spawnFallingItem();

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we play back
		// a sound effect as well.
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
