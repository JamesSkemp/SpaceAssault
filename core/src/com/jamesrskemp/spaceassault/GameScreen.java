package com.jamesrskemp.spaceassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by James on 3/15/2015.
 */
public class GameScreen implements Screen {
	public static final String TAG = GameScreen.class.getName();

	final SpaceAssaultGame game;

	private OrthographicCamera camera;

	private TextureAtlas playerShipAtlas;
	private TextureRegion playerShipImage;
	private Rectangle playerShip;

	private Vector3 lastTouchPosition;

	public GameScreen(final SpaceAssaultGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		playerShipAtlas = new TextureAtlas(Gdx.files.internal("SpaceAssaultPlayer.pack"));
		playerShipImage = playerShipAtlas.findRegion("playerShip3_blue");
		playerShip = new Rectangle();
		playerShip.x = 800 / 2 - 98 / 2;
		playerShip.y = 75 / 2;
		// Player ships are slightly different sizes, depending.
		playerShip.width = 98;
		playerShip.height = 75;

		lastTouchPosition = new Vector3();
	}

	@Override
	public void dispose() {
		playerShipAtlas.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(playerShipImage, playerShip.getX(), playerShip.getY());
		game.batch.end();

		if (Gdx.input.isTouched()) {
			lastTouchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(lastTouchPosition);
			playerShip.x = MathUtils.clamp(lastTouchPosition.x - playerShip.width / 2, 0, 800 - (playerShip.width));
			playerShip.y = MathUtils.clamp(lastTouchPosition.y - playerShip.height / 2, 0, 480 - playerShip.height);
			//Gdx.app.log(TAG, "Touch: <" + lastTouchPosition.x + "," + lastTouchPosition.y + ">");
			//Gdx.app.log(TAG, "Ship: <" + playerShip.x + "," + playerShip.y + ">");
		}
	}

	@Override
	public void show() {
		// TODO start music, if any is added.
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
	}
}
