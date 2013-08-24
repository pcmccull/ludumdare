package com.mccullickgames.godownwiththeship.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mccullickgames.godownwiththeship.GameSettings;
import com.mccullickgames.godownwiththeship.util.TilePool;
import com.monkeysonnet.engine.SpatialIndex;

public class WorldModel {
	private long timeLastStarted;
	private long millisecondsLeftWhenTimerStarted;
	private long millisecondsLeft;
	public Vector2 heroStartPosition;
	public Vector2 heroPosition;
	public Vector2 lastGoodHeroPosition;
	public Vector2 exitPoint;
	public boolean dragging;
	private Rectangle boundary;
	public SpatialIndex<Tile> tiles;
	public WorldModel() {
		heroPosition = new Vector2(0, 0);
		heroStartPosition = new Vector2(0, 0);
		lastGoodHeroPosition = new Vector2(0, 0);
		exitPoint = new Vector2(0, 0);	
		tiles = new SpatialIndex<Tile>(200);
	}
	public void beginLevel(int level) {		
		cleanupLastMap();
		loadLevelMap(level);
		resetLevel();
	}
	
	public void resetLevel() {
		millisecondsLeft = GameSettings.GAME_TIME;
		dragging = false;		
		heroPosition.set(heroStartPosition.x, heroStartPosition.y);
		lastGoodHeroPosition.set(heroStartPosition.x, heroStartPosition.y);
	}
	
	private void cleanupLastMap() {
		for (Tile tile: tiles.getAll()) {
			tile.reset();			
			TilePool.free(tile);
		}		
		tiles.clear();
		tiles = new SpatialIndex<Tile>(200);
	}
	
	private void loadLevelMap(int level) {
		Pixmap levelMap = new Pixmap(Gdx.files.internal("levels/level" + level + ".png"));
		tiles.clear();
		for (int x = 0; x < levelMap.getWidth(); x++) {
			for (int y = 0; y < levelMap.getHeight(); y++) {
				int value = levelMap.getPixel(x, y);
				Color c = new Color(value);
				int comparisonValue = c.toIntBits();
				
				float worldX = x * GameSettings.GAME_SCALE_LEVELMAP;
				float worldY = y * GameSettings.GAME_SCALE_LEVELMAP;
				Tile tile = null; 
				if (comparisonValue == GameSettings.LEVEL_OBJECTS.HERO.getValue()) {					
					heroPosition.set(worldX, worldY);
					heroStartPosition.set(heroPosition.x, heroPosition.y);
				} else if (comparisonValue == GameSettings.LEVEL_OBJECTS.EXIT.getValue()) {
					exitPoint.set(worldX, worldY);
				} else if (comparisonValue == GameSettings.LEVEL_OBJECTS.WALL_EW.getValue()) {
					tile = TilePool.get(worldX, worldY, "wallEW", GameSettings.objectHitAreas.get(GameSettings.LEVEL_OBJECTS.WALL_EW));				
				} else if (comparisonValue == GameSettings.LEVEL_OBJECTS.WALL_NS.getValue()) {
					tile = TilePool.get(worldX, worldY, "wallNS", GameSettings.objectHitAreas.get(GameSettings.LEVEL_OBJECTS.WALL_NS));
				} else if (comparisonValue == GameSettings.LEVEL_OBJECTS.WALL_CORNER_TR.getValue()) {
					tile = TilePool.get(worldX, worldY, "wallTR", GameSettings.objectHitAreas.get(GameSettings.LEVEL_OBJECTS.WALL_CORNER_TR));
				} else if (comparisonValue == GameSettings.LEVEL_OBJECTS.WALL_CORNER_TL.getValue()) {
					tile = TilePool.get(worldX, worldY, "wallTL", GameSettings.objectHitAreas.get(GameSettings.LEVEL_OBJECTS.WALL_CORNER_TL));
				} else if (comparisonValue == GameSettings.LEVEL_OBJECTS.WALL_CORNER_BR.getValue()) {
					tile = TilePool.get(worldX, worldY, "wallBR", GameSettings.objectHitAreas.get(GameSettings.LEVEL_OBJECTS.WALL_CORNER_BR));
				} else if (comparisonValue == GameSettings.LEVEL_OBJECTS.WALL_CORNER_BL.getValue()) {
					tile = TilePool.get(worldX, worldY, "wallBL", GameSettings.objectHitAreas.get(GameSettings.LEVEL_OBJECTS.WALL_CORNER_BL));
				}
				if (tile != null) {
					tiles.put(tile, worldX, worldY, tile.sprite.getWidth(), tile.sprite.getHeight());
				}
			}
		}
		
		boundary = new Rectangle(0, 0, levelMap.getWidth() * GameSettings.GAME_SCALE_LEVELMAP, 
				levelMap.getHeight() * GameSettings.GAME_SCALE_LEVELMAP);
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
	
	public Vector2 moveHeroOutOfWalls(Rectangle heroRectangle) {
		heroPosition.x = heroRectangle.x;
		heroPosition.y = heroRectangle.y;
		
		if (heroRectangle.x < 0) {
			heroPosition.x = 0;
		} else if (heroRectangle.x > boundary.width - heroRectangle.width) {
			heroPosition.x = boundary.width - heroRectangle.width;
		}
		
		if (heroRectangle.y < 0) {
			heroPosition.y = 0;
		}  else if (heroRectangle.y > boundary.height - heroRectangle.height) {
			heroPosition.y = boundary.height - heroRectangle.height;
		}
		
		
		
		
		int checkArea = 200;
		Array<Tile> possibleHits = tiles.get(lastGoodHeroPosition.x - checkArea, 
											lastGoodHeroPosition.y - checkArea,
											lastGoodHeroPosition.x + checkArea * 2,
											lastGoodHeroPosition.y + checkArea * 2);
		
		for (Tile wall : possibleHits) {
			if (wall.hitArea.overlaps(heroRectangle)) {
				heroPosition.x = lastGoodHeroPosition.x;
				heroPosition.y = lastGoodHeroPosition.y;
			} else if (checkLineToRectIntersection(lastGoodHeroPosition.x, lastGoodHeroPosition.y,
											heroPosition.x, heroPosition.y,
						wall.hitArea)) {
				//check for tile collisions between last known good location and current location
				heroPosition.x = lastGoodHeroPosition.x;
				heroPosition.y = lastGoodHeroPosition.y;
			}
		}
		
		lastGoodHeroPosition.x = heroPosition.x;
		lastGoodHeroPosition.y = heroPosition.y;
		return heroPosition;
	}
	
	public Rectangle getBoundaryRectangle() {		
		return boundary;
	}
	
	//http://stackoverflow.com/questions/1585525/how-to-find-the-intersection-point-between-a-line-and-a-rectangle
	public boolean checkLineToRectIntersection(float x1, float y1, float x2, float y2, 
									Rectangle rect) {
		float rectMinX = rect.x;
		float rectMinY = rect.y;
		float rectMaxX = rect.x + rect.width;		
		float rectMaxY = rect.y + rect.height;
		
		if ((x1 <= rectMinX && x2 <= rectMinX) 
		  || (y1 <= rectMinY && y2 <= rectMinY)
		  || (x1 >= rectMaxX && x2 >= rectMaxX)
		  || (y1 >= rectMaxY && y2 >= rectMaxY)) {
			return false;
		}
		
		float m = (y2 - y1) / (x2 - x1);
		float y = m * (rectMinX - x1) + y1;
	    if (y > rectMinY && y < rectMaxY) return true;

	    y = m * (rectMaxX - x1) + y1;
	    if (y > rectMinY && y < rectMaxY) return true;

	    float x = (rectMinY - y1) / m + x1;
	    if (x > rectMinX && x < rectMaxX) return true;

	    x = (rectMaxY - y1) / m + x1;
	    if (x > rectMinX && x < rectMaxX) return true;

	    return false;
		
		
	}
	
	public static Vector3 lvlMapToScreen(float x, float y, float z) {
		return new Vector3( x * GameSettings.GAME_SCALE_LEVELMAP, y * GameSettings.GAME_SCALE_LEVELMAP, 0);
	}
	
}
