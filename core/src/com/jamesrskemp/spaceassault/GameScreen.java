package com.jamesrskemp.spaceassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by James on 3/15/2015.
 */
public class GameScreen implements Screen {
	public static final String TAG = GameScreen.class.getName();

	final SpaceAssaultGame game;

	private OrthographicCamera camera;

	private Stage stage;

	private TextureAtlas playerShipAtlas;
	private TextureRegion playerShipImage;
	private Actor playerShip;

	private Vector3 lastTouchPosition;

	public GameScreen(final SpaceAssaultGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		stage = new Stage(new StretchViewport(800, 480));

		playerShipAtlas = new TextureAtlas(Gdx.files.internal("SpaceAssaultPlayer.pack"));
		playerShipImage = playerShipAtlas.findRegion("playerShip3_blue");
		playerShip = new Actor() {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.draw(playerShipImage, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
			}
		};
		playerShip.setBounds(playerShip.getX(), playerShip.getY(), playerShipImage.getRegionWidth(), playerShipImage.getRegionHeight());

		lastTouchPosition = new Vector3();
	}

	@Override
	public void dispose() {
		stage.dispose();
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
			playerShip.setX(MathUtils.clamp(lastTouchPosition.x - playerShip.getWidth() / 2, 0, 800 - (playerShip.getWidth())));
			playerShip.setY(MathUtils.clamp(lastTouchPosition.y - playerShip.getHeight() / 2, 0, 480 - playerShip.getHeight()));
			Gdx.app.log(TAG, "Touch: <" + lastTouchPosition.x + "," + lastTouchPosition.y + ">");
			Gdx.app.log(TAG, "Ship: <" + playerShip.getX() + "," + playerShip.getY() + ">");
		}
	}

	@Override
	public void show() {
		stage.addActor(playerShip);
		// TODO start music, if any is added.
	}

	@Override
	public void hide() {
		stage.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}
}
