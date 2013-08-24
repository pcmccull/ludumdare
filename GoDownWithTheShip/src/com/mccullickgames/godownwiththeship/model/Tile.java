package com.mccullickgames.godownwiththeship.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.mccullickgames.godownwiththeship.util.SpritePool;

public class Tile implements Poolable {	
	public Sprite sprite;
	private String spriteId;
	public float x;
	public float y;
	
	public Boolean alive;
	public Rectangle hitArea;
	
	public void init(float x, float y, String spriteId, Rectangle hitRectangle) {
		this.spriteId = spriteId;
		this.sprite = SpritePool.get(spriteId);
		this.x = x;
		this.y = y;
		this.hitArea = new Rectangle(hitRectangle);
		this.hitArea.x += x;
		this.hitArea.y += y;
	}

	@Override
	public void reset() {
		SpritePool.free(spriteId, sprite);
		x = 0; 
		y = 0;
		sprite = null;
		hitArea = null;
	}
}
