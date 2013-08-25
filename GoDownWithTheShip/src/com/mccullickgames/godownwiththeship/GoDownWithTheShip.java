package com.mccullickgames.godownwiththeship;

import com.badlogic.gdx.Game;
import com.mccullickgames.godownwiththeship.model.GameModel;
import com.mccullickgames.godownwiththeship.screens.PlayingScreen;

public class GoDownWithTheShip extends Game {

	@Override
	public void create() {
		Assets.load();
		GameModel model = new GameModel();
		
		PlayingScreen playScreen = new PlayingScreen(model);
		setScreen(playScreen);	
		
	}
	
}
