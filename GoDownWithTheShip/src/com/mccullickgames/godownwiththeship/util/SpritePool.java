package com.mccullickgames.godownwiththeship.util;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.mccullickgames.godownwiththeship.Assets;

public class SpritePool {
	private Map<String, Array<Sprite>> spritesMap;
	private static SpritePool instance;
	private SpritePool() {
		spritesMap = new HashMap<String, Array<Sprite>>();
	}
	
	public static SpritePool getInstance() {
		if (instance == null) {
			instance = new SpritePool();
		}
		return instance;
	}
	
	public static Sprite get(String id) {
		SpritePool pool = getInstance();
		Array<Sprite> spriteList = pool.spritesMap.get(id);
		if (spriteList == null) {
			pool.spritesMap.put(id, new Array<Sprite>());
			return Assets.images.get(id);
		} else {
			if (spriteList.size == 0) {
				return Assets.images.get(id);
			} else {
				return spriteList.pop();
			}
		}
	}
	
	public static void free(String id, Sprite sprite) {
		SpritePool pool = getInstance();
		Array<Sprite> spriteList = pool.spritesMap.get(id);
		if (spriteList == null) {
			spriteList = new Array<Sprite>();
			pool.spritesMap.put(id, spriteList);			
		}
		
		spriteList.add(sprite);
	}
	
}
