package com.mccullickgames.godownwiththeship.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mccullickgames.godownwiththeship.Assets;
import com.mccullickgames.godownwiththeship.GameSettings;
import com.mccullickgames.godownwiththeship.graphics.DeadOutOfTime;
import com.mccullickgames.godownwiththeship.graphics.DeadShocked;
import com.mccullickgames.godownwiththeship.graphics.EndOfGame;
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
	private Sprite stairsStartingPoint;
	private Sprite instructions;
	private Sprite touchToStart;
	private Sprite speechBubble;
	private GameTimerHud timerHud;
	private WorldModel world;
	private GameModel model;
	private DeadOutOfTime deadTimeAnim;
	private DeadShocked shockedAnim;
	private EndOfGame endOfGame;
	private OutOfBounds boundary;
	private boolean centeringCamera;
	private Vector3 desiredCameraPosition;
	private float centeringSpeedAdjustment;
	private GAME_STATE state;
	private BitmapFont font;
	
	private List<String> saySomething;
	private float showSaySomethingTimer;
	private float showSaySomethingDesiredTime;
	
	private enum GAME_STATE {
		INSTRUCTIONS, PLAYING, DEAD_WATER, DEAD_ELECTRIC, SUCCESS, GAME_OVER
	}
	
	public PlayingScreen(GameModel model) {
		this.model = model;
		world = new WorldModel();
		world.beginLevel(model.getCurrentLevel());
		centeringCamera = false;
		desiredCameraPosition = new Vector3();
		boundary = new OutOfBounds();
		font = new BitmapFont();
		font.setColor(0.2f, 0.2f,0.2f, 1.0f);
		saySomething = new ArrayList<String>();
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
		stairsStartingPoint = Assets.images.get("stairsStartingPoint");
		speechBubble = Assets.images.get("speechBubble");
		timerHud = new GameTimerHud();
		deadTimeAnim = new DeadOutOfTime();
		shockedAnim = new DeadShocked();
		endOfGame = new EndOfGame();
		
		startLevel();
	}
	
	public void startLevel() {
		instructions = Assets.images.get(GameSettings.levelTitles.get(model.getCurrentLevel()));
		instructions.setPosition(150, -187);
		touchToStart = Assets.images.get("instructions_touchToStart");
		touchToStart.setPosition(150,  -320);
		stairs.setPosition(world.exitPoint.x - stairs.getWidth()/2, world.exitPoint.y - stairs.getHeight()/2);
		world.resetLevel();
		hero.setPosition(world.heroPosition.x - hero.getWidth()/2, world.heroPosition.y - hero.getHeight()/2);
		
		stairsStartingPoint.setPosition(hero.getX() - 15, hero.getY() - stairsStartingPoint.getHeight()  + 5);
		desiredCameraPosition.set(instructions.getX() + 190, instructions.getY(), 0);
		centeringCamera = true;
		centeringSpeedAdjustment = 1;
		state = GAME_STATE.INSTRUCTIONS;
		boundary.init(world.getBoundaryRectangle());		
		timerHud.updateWaveOffset();
		saySomething.clear();
		hero.setRotation(0);
	}	

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render(float dt) {
		updateGame(dt);
		Gdx.gl.glClearColor(0.95f, 0.96f, 0.98f, 1);
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
			if (model.getCurrentLevel() != 0) {
				stairsStartingPoint.draw(batch);
			}
			if (saySomething.size() > 0) {
				speechBubble.setPosition(hero.getX() + 20, hero.getY() + 20);
				speechBubble.draw(batch);
				font.draw(batch, saySomething.get(0), hero.getX() + 54, hero.getY() + 55);
			}
		
			hero.draw(batch);

			
			instructions.draw(batch);
			touchToStart.draw(batch);
			
			
		batch.end();
				
		//draw HUD		
		batch.getProjectionMatrix().setToOrtho(0, GameSettings.GAME_WIDTH, 0,  GameSettings.GAME_HEIGHT, 0, 1);		
		
		timerHud.render(dt, batch, world.getMillisecondsLeft());
		if (state == GAME_STATE.DEAD_WATER) {
			deadTimeAnim.render(dt, batch);
			
		} else if (state == GAME_STATE.GAME_OVER) {
			endOfGame.render(dt, batch);
		} else if (state == GAME_STATE.DEAD_ELECTRIC) {
			shockedAnim.render(dt, batch);
		}
	}
	
	public void startNextLevel() {
		model.nextLevel();
		world.beginLevel(model.getCurrentLevel());
		startLevel();
	}

	private void updateGame(float delta) {	
		if (saySomething.size() > 0) {
			showSaySomethingTimer += delta;
			
			if (showSaySomethingDesiredTime < showSaySomethingTimer) {
				saySomething.remove(0);
				if (saySomething.size() > 0) {
					showSaySomethingTimer = 0;
					showSaySomethingDesiredTime = 3;
				}
			}
		}
		world.updateWorld(delta);
		if (world.getMillisecondsLeft() <= 0 && state == GAME_STATE.PLAYING) {
			Gdx.app.log("PlayingScreen", "out of time!!");
			model.playerDied();
			state = GAME_STATE.DEAD_WATER;
			deadTimeAnim.start();

			return;
		} else if (world.touchingElectrical && state == GAME_STATE.PLAYING) {
			Gdx.app.log("PlayingScreen", "shocked!!");
			model.playerDied();
			state = GAME_STATE.DEAD_ELECTRIC;
			shockedAnim.start();
			
		}
		
		if (world.dragging) {
			Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f);
			camera.unproject(touchPoint);
			float newX = touchPoint.x - hero.getWidth() / 2;
			float newY = touchPoint.y - hero.getHeight() / 2;
			float dx = hero.getX() - newX;
			float dy = hero.getY() - newY;
			if (!(dx < 1 && dx > -1 && dy < 1 && dy > -1)) {
				
				float degrees = (float) (Math.atan(dy/dx)*180/Math.PI);
				degrees += 90;
				if (dx < 0) {
					degrees -= 180;
				}
				hero.setRotation(degrees);
			}	
		
			hero.setPosition(newX, newY);
			
			
		}
		
		Vector2 newPosition = world.moveHeroOutOfWalls(hero.getX(), hero.getY(), hero.getBoundingRectangle());
		hero.setPosition(newPosition.x, newPosition.y);

		if (centeringCamera) {			
			Vector3 position = camera.position;
			position.x += (desiredCameraPosition.x - position.x) * centeringSpeedAdjustment;
			position.y += (desiredCameraPosition.y - position.y) * centeringSpeedAdjustment;
			
			if (position.epsilonEquals(desiredCameraPosition, 1.0f)) {
				centeringCamera = false;
				camera.position.set(desiredCameraPosition.x, desiredCameraPosition.y, desiredCameraPosition.z);
				if (state == GAME_STATE.SUCCESS) {
					onLevelEnded();
				}
			}
			camera.update();
			centeringSpeedAdjustment += GameSettings.CAMERA_SPEED_INCREASE;
		}
	}
	
	private void checkForEndOfGame() {
		if (stairs.getBoundingRectangle().overlaps(hero.getBoundingRectangle())) {
			Gdx.app.log("PlayingScreen", "success!");
			state = GAME_STATE.SUCCESS;
			centerCameraOnHero();
			say("I made it!");
		} 		
	}
	private void onLevelEnded() {
		if (model.getCurrentLevel() < GameSettings.TOTAL_LEVELS - 1) {			
			startNextLevel();
		} else {
			state = GAME_STATE.GAME_OVER;
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
		
		if (state == GAME_STATE.INSTRUCTIONS) {
			state = GAME_STATE.PLAYING;
			centeringSpeedAdjustment = GameSettings.DEFAULT_CAMERA_SPEED_ADJUSTMENT;
			centerCameraOnHero();
			if (model.getCurrentLevel() == 0) {
				say("Shiver me timbers.");
				say("I need to go UP.");
			}
			return false;
		} else if (state == GAME_STATE.PLAYING) {
			Vector3 touchPoint = new Vector3(screenX, screenY, 0.0f);
			camera.unproject(touchPoint); 
			
			if (increaseHeroHitArea(hero.getBoundingRectangle()).contains(touchPoint.x, touchPoint.y)) {		
				world.startDragging();
			}
		} else if (state == GAME_STATE.DEAD_WATER && deadTimeAnim.isComplete()) {
			startLevel();
		} else if (state == GAME_STATE.DEAD_ELECTRIC && shockedAnim.isComplete()) {
			startLevel();
		}  else if (state == GAME_STATE.GAME_OVER && endOfGame.isComplete()) {
			model.setCurrentLevel(-1);
			startNextLevel();
			
		}
		return false;
	}
	
	private void say(String something) {
		saySomething.add(something);
		if (saySomething.size() == 1) {
			showSaySomethingTimer = 0;
			showSaySomethingDesiredTime = 3;
		}
	}

	private Rectangle increaseHeroHitArea(Rectangle boundingRectangle) {

		boundingRectangle.x -= GameSettings.INCREASE_HERO_TOUCH_SIZE;
		boundingRectangle.y -= GameSettings.INCREASE_HERO_TOUCH_SIZE;
		boundingRectangle.height += GameSettings.INCREASE_HERO_TOUCH_SIZE * 2;
		boundingRectangle.width += GameSettings.INCREASE_HERO_TOUCH_SIZE * 2;
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
