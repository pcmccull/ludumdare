package com.mccullickgames.godownwiththeship.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mccullickgames.godownwiththeship.Assets;
import com.mccullickgames.godownwiththeship.GameSettings;
import com.mccullickgames.godownwiththeship.hud.GameTimerHud;
import com.mccullickgames.godownwiththeship.model.WorldModel;

public class PlayingScreen implements Screen, InputProcessor {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Sprite hero;
	private Sprite stairs;
	private Sprite gameMap;
	private GameTimerHud timerHud;
	private WorldModel world;
	
	public PlayingScreen() {
		world = new WorldModel();
		world.beginLevel(0);
	}
	
	@Override
	public void show() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
						
		hero = Assets.images.get("hero");		
		hero.setOrigin(hero.getWidth()/2, hero.getHeight()/2);
		hero.setPosition(-hero.getWidth()/2, -hero.getHeight()/2);
		Gdx.input.setInputProcessor(this);
		
		stairs = Assets.images.get("stairsNS");
		timerHud = new GameTimerHud();
		gameMap = Assets.images.get("level01");
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
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
			stairs.draw(batch);
			hero.draw(batch);
		batch.end();
		
		//draw HUD		
		batch.getProjectionMatrix().setToOrtho(0, GameSettings.GAME_WIDTH, 0,  GameSettings.GAME_HEIGHT, 0, 1);		
		batch.begin();		
			gameMap.draw(batch);
		batch.end();
		timerHud.render(dt, batch, world.getMillisecondsLeft());
	}

	private void updateGame(float delta) {		
		world.updateWorld(delta);
		if (stairs.getBoundingRectangle().overlaps(hero.getBoundingRectangle())) {
			Gdx.app.log("PlayingScreen", "success!");
		} else if (world.getMillisecondsLeft() <= 0) {
			Gdx.app.log("PlayingScreen", "out of time!!");
		}
		
		if (world.dragging) {
			Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f);
			camera.unproject(touchPoint);
			hero.setPosition(touchPoint.x - hero.getWidth()/2, touchPoint.y - hero.getHeight()/2);			
		}
		
		//TODO check for wall/thing intersection with hero
		
		Gdx.app.log("Timer", world.getMillisecondsLeft() + "");
	}

	@Override
	public void resize(int width, int height) {
		float aspect = (float)height/(float)width;
		camera = new OrthographicCamera(GameSettings.GAME_WIDTH, aspect * GameSettings.GAME_WIDTH);
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
		Vector3 touchPoint = new Vector3(screenX, screenY, 0.0f);
		camera.unproject(touchPoint); 
		
		if (hero.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {		
			world.startDragging();
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {		
		world.stopDragging();
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
