package com.mccullickgames.godownwiththeship.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mccullickgames.godownwiththeship.Assets;
import com.mccullickgames.godownwiththeship.GameSettings;
import com.mccullickgames.godownwiththeship.graphics.GameTimerHud;
import com.mccullickgames.godownwiththeship.graphics.OutOfBounds;
import com.mccullickgames.godownwiththeship.model.GameModel;
import com.mccullickgames.godownwiththeship.model.Tile;
import com.mccullickgames.godownwiththeship.model.WorldModel;

public class PlayingScreen implements Screen, InputProcessor {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Sprite hero;
	private Sprite stairs;
	private Sprite instructions;
	private Sprite touchToStart;
	private GameTimerHud timerHud;
	private WorldModel world;
	private GameModel model;
	private OutOfBounds boundary;
	private boolean centeringCamera;
	private Vector3 desiredCameraPosition;
	private float centeringSpeedAdjustment;
	private Boolean gameStarted;
	
	public PlayingScreen(GameModel model) {
		this.model = model;
		world = new WorldModel();
		world.beginLevel(model.getCurrentLevel());
		centeringCamera = false;
		desiredCameraPosition = new Vector3();
		boundary = new OutOfBounds();
	}
	
	@Override
	public void show() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
						
		hero = Assets.images.get("hero");
		
		Gdx.input.setInputProcessor(this);
		
		stairs = Assets.images.get("stairsNS");
		timerHud = new GameTimerHud();
		
		
		startLevel();
	}
	
	public void startLevel() {
		instructions = Assets.images.get(GameSettings.levelTitles.get(model.getCurrentLevel()));
		instructions.setPosition(210, -200);
		touchToStart = Assets.images.get("instructions_touchToStart");
		touchToStart.setPosition(210,  -320);
		stairs.setPosition(world.exitPoint.x - stairs.getWidth()/2, world.exitPoint.y - stairs.getHeight()/2);
		world.resetLevel();
		hero.setPosition(world.heroPosition.x - hero.getWidth()/2, world.heroPosition.y - hero.getHeight()/2);
		desiredCameraPosition.set(instructions.getX() + 190, instructions.getY(), 0);
		centeringCamera = true;
		centeringSpeedAdjustment = 1;
		gameStarted = false;
		boundary.init(world.getBoundaryRectangle());		
		
	}	

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render(float dt) {
		updateGame(dt);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.apply(Gdx.gl10);
		
		
		batch.setProjectionMatrix(camera.combined);
		//draw out of bounds area
		boundary.render(); 
		
		batch.begin();
			for (Tile tile: world.tiles.get(camera.position.x -  camera.viewportWidth/2 ,camera.position.y - camera.viewportHeight/2, camera.viewportWidth, camera.viewportHeight)) {
				tile.sprite.setPosition(tile.x, tile.y);
				tile.sprite.draw(batch);
			}
			
			stairs.draw(batch);
			hero.draw(batch);
			
			instructions.draw(batch);
			touchToStart.draw(batch);
		batch.end();
				
		//draw HUD		
		batch.getProjectionMatrix().setToOrtho(0, GameSettings.GAME_WIDTH, 0,  GameSettings.GAME_HEIGHT, 0, 1);		
		
		timerHud.render(dt, batch, world.getMillisecondsLeft());
	}
	
	public void startNextLevel() {
		model.nextLevel();
		world.beginLevel(model.getCurrentLevel());
		startLevel();
	}

	private void updateGame(float delta) {		
		world.updateWorld(delta);
		if (world.getMillisecondsLeft() <= 0) {
			Gdx.app.log("PlayingScreen", "out of time!!");
			model.playerDied();
			startLevel();
			return;
		}
		
		if (world.dragging) {
			Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f);
			camera.unproject(touchPoint);
			hero.setPosition(touchPoint.x - hero.getWidth()/2, touchPoint.y - hero.getHeight()/2);			
		}
		
		Vector2 newPosition = world.moveHeroOutOfWalls(hero.getBoundingRectangle());
		hero.setPosition(newPosition.x, newPosition.y);

		if (centeringCamera) {			
			Vector3 position = camera.position;
			position.x += (desiredCameraPosition.x - position.x) * centeringSpeedAdjustment;
			position.y += (desiredCameraPosition.y - position.y) * centeringSpeedAdjustment;
			
			if (position.epsilonEquals(desiredCameraPosition, 1.0f)) {
				centeringCamera = false;
				camera.position.set(desiredCameraPosition.x, desiredCameraPosition.y, desiredCameraPosition.z);
			}
			camera.update();
			centeringSpeedAdjustment += GameSettings.CAMERA_SPEED_INCREASE;
		}
	}
	
	private void checkForEndOfGame() {
		if (stairs.getBoundingRectangle().overlaps(hero.getBoundingRectangle())) {
			Gdx.app.log("PlayingScreen", "success!");
			if (model.getCurrentLevel() < GameSettings.TOTAL_LEVELS - 1)
				startNextLevel();
			else {
				//TODO: for testing the levels will loop
				model.setCurrentLevel(-1);
				startNextLevel();
			}
		} 		
	}

	private void centerCameraOnHero() {
		desiredCameraPosition.set(hero.getX(), hero.getY(), camera.position.z);
		centeringCamera = true;
	}

	@Override
	public void resize(int width, int height) {
		float aspect = (float)height/(float)width;
		camera.viewportWidth = GameSettings.GAME_WIDTH * GameSettings.GAME_SCALE;
		camera.viewportHeight = aspect * GameSettings.GAME_WIDTH * GameSettings.GAME_SCALE;
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {		
		
		if (!gameStarted) {
			gameStarted = true;
			centeringSpeedAdjustment = GameSettings.DEFAULT_CAMERA_SPEED_ADJUSTMENT;
			centerCameraOnHero();
			return false;
		}
		Vector3 touchPoint = new Vector3(screenX, screenY, 0.0f);
		camera.unproject(touchPoint); 
		
		if (increaseHeroHitArea(hero.getBoundingRectangle()).contains(touchPoint.x, touchPoint.y)) {		
			world.startDragging();
		}
		return false;
	}

	private Rectangle increaseHeroHitArea(Rectangle boundingRectangle) {
		int increase = 20;
		boundingRectangle.x -= increase;
		boundingRectangle.y -= increase;
		boundingRectangle.height += increase * 2;
		boundingRectangle.width += increase * 2;
		return boundingRectangle;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (world.dragging) {
			world.stopDragging();
			centerCameraOnHero();
			centeringSpeedAdjustment = GameSettings.DEFAULT_CAMERA_SPEED_ADJUSTMENT;
			checkForEndOfGame();
		}
		return false;
	}



	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	


}
