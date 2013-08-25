package com.mccullickgames.godownwiththeship.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mccullickgames.godownwiththeship.model.Tile;

public class TilePool {
	private Array<Tile> tiles;
	private static TilePool instance;
	private TilePool() {
		tiles = new Array<Tile>();
	}
	
	public static TilePool getInstance() {
		if (instance == null) {
			instance = new TilePool();
		}
		return instance;
	}
	
	public static Tile get(float x, float y, String spriteId, Rectangle hitRectangle) {
		TilePool pool = getInstance(); 
		Tile tile;
		if (pool.tiles.size == 0) {
			tile = new Tile();
		} else {
			tile = pool.tiles.pop();
		}	
		tile.init(x, y, spriteId, hitRectangle);
		return tile;
	}
	
	public static void free(Tile tile) {		
		TilePool pool = getInstance();
		pool.tiles.add(tile);
	}

	public static Tile clone(Tile tile, float x, float y) {
		TilePool pool = getInstance(); 
		Tile newTile;
		if (pool.tiles.size == 0) {
			newTile = new Tile();
		} else {
			newTile = pool.tiles.pop();
		}	
		
		newTile.init(x, y, tile.spriteId, tile.hitArea);
		return newTile;
	}

	
}
