package com.mccullickgames.godownwiththeship;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Assets {
	public static Map<String, Sprite> images = new HashMap<String, Sprite>();
	public static TextureAtlas spriteSheet;
	
	public static void load() {	
		spriteSheet = new TextureAtlas(Gdx.files.internal("sprites/godownwiththeship.pack"));
		for (AtlasRegion region: spriteSheet.getRegions()) {	
			images.put(region.name, spriteSheet.createSprite(region.name));
		}
		
	}
	public static void dispose() {
		spriteSheet.dispose();
	}
}