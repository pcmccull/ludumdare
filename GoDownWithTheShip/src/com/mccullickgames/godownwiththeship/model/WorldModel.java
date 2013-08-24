package com.mccullickgames.godownwiththeship.model;

import com.badlogic.gdx.utils.TimeUtils;
import com.mccullickgames.godownwiththeship.GameSettings;

public class WorldModel {
	private long timeLastStarted;
	private long millisecondsLeftWhenTimerStarted;
	private long millisecondsLeft;	
	public boolean dragging;
	
	public void beginLevel(int level) {
		millisecondsLeft = GameSettings.GAME_TIME;
		dragging = false;
	}
	
	public void startDragging() {
		timeLastStarted = TimeUtils.millis();
		millisecondsLeftWhenTimerStarted = millisecondsLeft;
		dragging = true;
	}
	
	public void stopDragging() {
		dragging = false;
	}
	
	public void updateWorld(float delta) {
		if (dragging) {			
			millisecondsLeft = millisecondsLeftWhenTimerStarted - 
									(TimeUtils.millis() - timeLastStarted); 
		}
	}
	
	public long getMillisecondsLeft() {
		return millisecondsLeft;
	}
}