package com.mccullickgames.godownwiththeship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mccullickgames.godownwiththeship.model.Tile;
import com.mccullickgames.godownwiththeship.util.TilePool;

public class GameSettings {

	public static final int GAME_WIDTH = 960;
	public static final int GAME_HEIGHT = 640;
	
	public static final int GAME_TIME = 10 * 1000; //milliseconds that the game lasts
	
	public static final float GAME_SCALE = 0.5f;//1 meter is one pixel on the level map and is 40 pixels for artwork and is about 80 pixels on screen
	public static final float GAME_SCALE_LEVELMAP = 40;//1 meter is one pixel on the level map
	public static final float TILE_SQUARED = GAME_SCALE_LEVELMAP * GAME_SCALE_LEVELMAP;
	public static final int TOTAL_LEVELS = 6;
	public static final int STARTING_LEVEL = 0;
	
	public static final Array<String> levelTitles = new Array<String>() {{
		add("firstFloorInstructions");
		add("secondFloorInstructions");
		add("thirdFloorInstructions");
		add("fourthFloorInstructions");
		add("fifthFloorInstructions");
		add("sixthFloorInstructions");
	}};
	
	public static final float DEFAULT_CAMERA_SPEED_ADJUSTMENT = 0.02f;
	public static final float CAMERA_SPEED_INCREASE = 0.005f;
	
	public enum LEVEL_OBJECTS {
		HERO(Color.toIntBits(255, 0, 0, 255)), 
		EXIT(Color.toIntBits(0, 255, 0, 255)),
		WALL_EW(Color.toIntBits(255, 0, 255, 255), "wallEW", new Rectangle(0, 18, 40, 4)),
		WALL_NS(Color.toIntBits(255, 0, 254, 255), "wallNS", new Rectangle(18, 0, 4, 40)),
		WALL_CORNER_TR(Color.toIntBits(255, 0, 253, 255),"wallTR", new Rectangle(0, 20, 20, 20)),
		WALL_CORNER_TL(Color.toIntBits(255, 0, 252, 255), "wallTL", new Rectangle(20, 20, 20, 20)),
		WALL_CORNER_BR(Color.toIntBits(255, 0, 251, 255), "wallBR", new Rectangle(0, 0, 20, 20)),
		WALL_CORNER_BL(Color.toIntBits(255, 0, 250, 255), "wallBL", new Rectangle(20, 0, 20, 20)),
		WALL_EW_TT(Color.toIntBits(255, 0, 249, 255), "wallEW_TT", new Rectangle(0, 20, 20, 20)),
		WALL_EW_TB(Color.toIntBits(255, 0, 248, 255), "wallEW_TB", new Rectangle(0, 20, 20, 20)),
		WALL_NS_TL(Color.toIntBits(255, 0, 247, 255), "wallNS_TL", new Rectangle(0, 20, 20, 20)),
		WALL_NS_TR(Color.toIntBits(255, 0, 246, 255), "wallNS_TR", new Rectangle(0, 20, 20, 20)),
		WALL_NS_EW(Color.toIntBits(255, 0, 245, 255), "wallNS_EW", new Rectangle(0, 0, 40, 40)),
		ELECTRICAL(Color.toIntBits(0, 0, 255, 255), "electricalWarning", new Rectangle(0, 0, 40, 40));
		
		private int value;
		private Tile tile;
		private LEVEL_OBJECTS(int rgba) {
			this.value = rgba;
		}
		private LEVEL_OBJECTS(int rgba, String spriteId, Rectangle hitArea) {
			this.value = rgba;
			this.tile = TilePool.get(0,0, spriteId, hitArea);			
		}
		
		public int getValue() {
			return value;
		}
		public Tile getTile() {
			return tile;
		}
	}
	
	
	public static final float WATER_COLOR =  Color.toFloatBits(100, 100, 255, 150);
	public static final float BOUNDARY_COLOR =  Color.toFloatBits(50, 50, 50, 150);
	
	public static final float INCREASE_HERO_TOUCH_SIZE = 30;
	
}
