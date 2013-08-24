package com.mccullickgames.godownwiththeship;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class GameSettings {

	public static final int GAME_WIDTH = 960;
	public static final int GAME_HEIGHT = 640;
	
	public static final int GAME_TIME = 10 * 1000; //milliseconds that the game lasts
	
	public static final float GAME_SCALE = .5f;//1 meter is one pixel on the level map and is 40 pixels for artwork and is about 80 pixels on screen
	public static final float GAME_SCALE_LEVELMAP = 40;//1 meter is one pixel on the level map
	public static final float TILE_SQUARED = GAME_SCALE_LEVELMAP*GAME_SCALE_LEVELMAP;
	public static final int TOTAL_LEVELS = 4;
	public static final int STARTING_LEVEL = 0;
	
	public static final Array<String> levelTitles = new Array<String>() {{
		add("firstFloorInstructions");
		add("secondFloorInstructions");
		add("firstFloorInstructions");
		add("firstFloorInstructions");
	}};
	
	public static final Map<LEVEL_OBJECTS, Rectangle> objectHitAreas = new HashMap<LEVEL_OBJECTS, Rectangle>() {
		private static final long serialVersionUID = 1L;
	{
		put(LEVEL_OBJECTS.WALL_EW, new Rectangle(0, 19, 40, 2));
		put(LEVEL_OBJECTS.WALL_NS, new Rectangle(19, 0, 2, 40));
		put(LEVEL_OBJECTS.WALL_CORNER_BL, new Rectangle(20, 0, 20, 20));
		put(LEVEL_OBJECTS.WALL_CORNER_BR, new Rectangle(0, 0, 20, 20));
		put(LEVEL_OBJECTS.WALL_CORNER_TL, new Rectangle(20, 20, 20, 20));
		put(LEVEL_OBJECTS.WALL_CORNER_TR, new Rectangle(0, 20, 20, 20));
		
	}};
	public static final float DEFAULT_CAMERA_SPEED_ADJUSTMENT = 0.02f;
	public static final float CAMERA_SPEED_INCREASE = 0.005f;
	
	
	
	public enum LEVEL_OBJECTS {
		HERO(Color.toIntBits(255, 0, 0, 255)), 
		EXIT(Color.toIntBits(0, 255, 0, 255)),
		WALL_EW(Color.toIntBits(255, 0, 255, 255)),
		WALL_NS(Color.toIntBits(255, 0, 254, 255)),
		WALL_CORNER_TR(Color.toIntBits(255, 0, 253, 255)),
		WALL_CORNER_TL(Color.toIntBits(255, 0, 252, 255)),
		WALL_CORNER_BR(Color.toIntBits(255, 0, 251, 255)),
		WALL_CORNER_BL(Color.toIntBits(255, 0, 250, 255));
		
		int value;
		private LEVEL_OBJECTS(int rgba) {
			this.value = rgba;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	
	public static final float WATER_COLOR =  Color.toFloatBits(100, 100, 255, 150);
	public static final float BOUNDARY_COLOR =  Color.toFloatBits(50, 50, 50, 150);
	
}
