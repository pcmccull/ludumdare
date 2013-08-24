package com.mccullickgames.godownwiththeship;

import com.badlogic.gdx.Game;
import com.mccullickgames.godownwiththeship.screens.PlayingScreen;

public class SinkingShipEscape extends Game {

	@Override
	public void create() {
		Assets.load();
		PlayingScreen playScreen = new PlayingScreen();
		setScreen(playScreen);	
		
	}
	
}
