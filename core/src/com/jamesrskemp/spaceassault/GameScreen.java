package com.jamesrskemp.spaceassault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

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
	private Vector2 playerShipPosition = new Vector2();
	private Vector2 playerShipNewPosition = new Vector2();
	private Vector2 playerShipVelocity = new Vector2();
	private Vector2 playerShipDirection = new Vector2();
	private Vector2 playerShipMovement = new Vector2();
	private float playerShipDirectionAngle;
	private float playerShipSpeed = 50f;
	private boolean moveShip;

	private Vector3 lastTouchPosition = new Vector3();

	public GameScreen(final SpaceAssaultGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		playerShipAtlas = new TextureAtlas(Gdx.files.internal("SpaceAssaultPlayer.pack"));
		playerShipImage = playerShipAtlas.findRegion("playerShip3_blue");

		playerShip = new Rectangle();
		playerShip.width = playerShipImage.getRegionWidth();
		playerShip.height = playerShipImage.getRegionHeight();
		playerShip.x = (800 - 98) / 2;
		playerShip.y = 75 / 2;
		/*
		playerShip = new Actor() {
			@Override
			public void act(float delta) {
				Gdx.app.log(TAG, "Ship acting.");
				playerShipPosition.set(playerShip.getX(), playerShip.getY());
				playerShipDirection.set(lastTouchPosition.x, lastTouchPosition.y).sub(playerShipPosition).nor();
				playerShipVelocity.set(playerShipDirection).scl(playerShipSpeed);
				playerShipMovement.set(playerShipVelocity).scl(delta);
				if (playerShipPosition.dst2(lastTouchPosition.x, lastTouchPosition.y) > playerShipMovement.len2()) {
					Gdx.app.log(TAG, "Ship acting to add.");
					playerShipPosition.add(playerShipMovement);
					Gdx.app.log(TAG, "Movement: <" + playerShipMovement.x + "," + playerShipMovement.y + ">");
				} else {
					Gdx.app.log(TAG, "Ship acting to set.");
					playerShipPosition.set(lastTouchPosition.x, lastTouchPosition.y);
				}
			}
		};
		playerShip.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log(TAG, "Ship touched. <" + x + "," + y + ">");
				return true;
			}
		});
		playerShip.setPosition((Gdx.graphics.getWidth() - playerShip.getWidth()) / 2, (Gdx.graphics.getHeight() - playerShip.getHeight()) / 2);
		*/
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
		// TODO make this right; the rotation will go around the origin.x and origin.y.
		game.batch.draw(playerShipImage,
				playerShip.getX(), playerShip.getY(),
				0, 0,
				playerShip.width, playerShip.height,
				1, 1, 0);
		game.batch.end();

		if (moveShip) {
			playerShipPosition.set(playerShip.getX(), playerShip.getY());
			playerShipDirectionAngle = playerShipNewPosition.angle(playerShipPosition);
			Gdx.app.log(TAG, "Angle of direction: " + playerShipDirectionAngle);
			playerShipDirection.set(playerShipNewPosition.x, playerShipNewPosition.y).sub(playerShipPosition).nor();
			Gdx.app.log(TAG, "Ship direction: <" + playerShipDirection.x + "," + playerShipDirection.y + ">");

			playerShipVelocity.set(playerShipDirection).scl(playerShipSpeed);
			playerShipMovement.set(playerShipVelocity).scl(delta);
			if (playerShipPosition.dst2(playerShipNewPosition.x, playerShipNewPosition.y) > playerShipMovement.len2()) {
				Gdx.app.log(TAG, "Ship acting to add.");
				Gdx.app.log(TAG, "Ship old position: <" + playerShipPosition.x + "," + playerShipPosition.y + ">");
				playerShipPosition.add(playerShipMovement);
				Gdx.app.log(TAG, "Movement: <" + playerShipMovement.x + "," + playerShipMovement.y + ">");
				Gdx.app.log(TAG, "Ship new position: <" + playerShipPosition.x + "," + playerShipPosition.y + ">");
			} else {
				Gdx.app.log(TAG, "Ship acting to set.");
				playerShipPosition.set(playerShipNewPosition.x, playerShipNewPosition.y);
				moveShip = false;
			}
			playerShip.setX(playerShipPosition.x);
			playerShip.setY(playerShipPosition.y);
		}

		if (Gdx.input.isTouched()) {
			lastTouchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(lastTouchPosition);
			playerShipNewPosition.x = MathUtils.clamp(lastTouchPosition.x - playerShip.getWidth() / 2, 0, 800 - playerShip.getWidth());
			playerShipNewPosition.y = MathUtils.clamp(lastTouchPosition.y - playerShip.getHeight() / 2, 0, 480 - playerShip.getHeight());
			Gdx.app.log(TAG, "Touch: <" + lastTouchPosition.x + "," + lastTouchPosition.y + ">");
			Gdx.app.log(TAG, "Ship: <" + playerShip.getX() + "," + playerShip.getY() + ">");
			moveShip = true;
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
