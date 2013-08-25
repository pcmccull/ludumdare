package com.mccullickgames.godownwiththeship.graphics;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.mccullickgames.godownwiththeship.Assets;
import com.mccullickgames.godownwiththeship.GameSettings;

public class EndOfGame {
	
	private long startTime = 0;	
	private Sprite instructions;
	private Mesh mesh;
	public EndOfGame() {
		instructions = Assets.images.get("gameoverText");
		instructions.setPosition(200, 200);
		mesh = getBackgroundMesh();
	}
	
	public void start() {
		startTime = TimeUtils.millis();
	}

	public void render(float dt, SpriteBatch batch) {
		mesh.render(GL10.GL_TRIANGLE_STRIP);
		batch.begin();
			instructions.draw(batch);
		batch.end();
	}

	private Mesh getBackgroundMesh() {
		float boundaryColor = GameSettings.BOUNDARY_COLOR;
		int verticesCount = 6*4;
		float[] verts = new float[verticesCount];
		float outerWidth = GameSettings.GAME_WIDTH;
		float outerHeight = GameSettings.GAME_HEIGHT;
		int i = 0;
		
		//LEFT
		verts[i++] = 0; // x
		verts[i++] = 0; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		verts[i++] = outerWidth; // x
		verts[i++] = 0; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		verts[i++] = outerWidth ; // x
		verts[i++] = outerHeight ; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		verts[i++] = 0; // x
		verts[i++] = 0; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		verts[i++] = 0 ; // x
		verts[i++] = outerHeight ; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		verts[i++] = outerWidth ; // x
		verts[i++] = outerHeight ; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		
		mesh = (new Mesh(true, verticesCount, 0, 
				new VertexAttribute(Usage.Position, 3, "a_position"), 
				new VertexAttribute(Usage.ColorPacked, 4, "a_color")));

		mesh.setVertices(verts);
		return mesh;
	}

	public boolean isComplete() {
		long localDt = TimeUtils.millis() - startTime;

		return localDt > 1700;
	}
}
