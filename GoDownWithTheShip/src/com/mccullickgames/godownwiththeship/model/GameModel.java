package com.mccullickgames.godownwiththeship.model;

import com.badlogic.gdx.math.Vector3;
import com.mccullickgames.godownwiththeship.GameSettings;

public class GameModel {

	private int currentLevel;
	private int deaths;
	private int score;
	private boolean started;
	private boolean centeringCamera;	
	private Vector3 desiredCameraPosition;
	private float centeringSpeedAdjustment;
	private boolean isAlive;	
	
	public GameModel() {
		this.deaths = 0;
		this.currentLevel = GameSettings.STARTING_LEVEL;
		this.score = 0;
		this.isAlive = true;
	}
	
	public int getCurrentLevel() {
		return currentLevel;
	}
	
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	public int getDeaths() {
		return deaths;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public void playerDied() {
		this.deaths ++;
		this.setAlive(false);
	}

	public void nextLevel() {
		currentLevel++;		
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isCenteringCamera() {
		return centeringCamera;
	}

	public void setCenteringCamera(boolean centeringCamera) {
		this.centeringCamera = centeringCamera;
	}

	public Vector3 getDesiredCameraPosition() {
		return desiredCameraPosition;
	}

	public void setDesiredCameraPosition(Vector3 desiredCameraPosition) {
		this.desiredCameraPosition = desiredCameraPosition;
	}

	public float getCenteringSpeedAdjustment() {
		return centeringSpeedAdjustment;
	}

	public void setCenteringSpeedAdjustment(float centeringSpeedAdjustment) {
		this.centeringSpeedAdjustment = centeringSpeedAdjustment;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
}
