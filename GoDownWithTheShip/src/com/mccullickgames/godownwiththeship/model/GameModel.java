package com.mccullickgames.godownwiththeship.model;

import com.mccullickgames.godownwiththeship.GameSettings;

public class GameModel {

	private int currentLevel;
	private int deaths;
	private int score;
	
	public GameModel() {
		this.deaths = 0;
		this.currentLevel = GameSettings.STARTING_LEVEL;
		this.score = 0;
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
	}

	public void nextLevel() {
		currentLevel++;		
	}
}
