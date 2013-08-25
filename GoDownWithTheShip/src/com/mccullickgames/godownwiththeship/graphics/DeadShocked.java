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

public class DeadShocked {

	private float waveOffset = 0.4f; // the additional wave that makes it look more like water
	private long startTime = 0;	
	private Sprite instructions;
	public DeadShocked() {
		instructions = Assets.images.get("instructions_deadShocked");
		instructions.setPosition(200, 200);
	}
	public void start() {
		startTime = TimeUtils.millis();
		updateWaveOffset();
	}

	public void render(float dt, SpriteBatch batch) {
		long localDt = TimeUtils.millis() - startTime;
		
		createWaterMesh(localDt).render(GL10.GL_TRIANGLE_STRIP);
	
		if (localDt > 700) {
			batch.begin();
			instructions.draw(batch);
			batch.end();
		}
	}
	public void updateWaveOffset() {
		waveOffset = (float) (Math.random() * 0.6) + 0.1f;
	}
		

	public Mesh createWaterMesh(float currentTime) {
		float waterColor = GameSettings.WATER_COLOR;
		float frequency = 0.5f;
		float amplitude = 5f;
		float yOffset = (GameSettings.GAME_HEIGHT * 0.9f) * Math.min(1, currentTime / 700);
		float xOffset = 0;
		int precision = 30;
		float width = GameSettings.GAME_WIDTH*2.5f;
		float scale = width/precision/2 +.06f;
		float[] verts = new float[precision * 4 ];
		float offset = (float) (TimeUtils.millis()/100 % (4 * Math.PI));
				
		for (int x = 0; x < precision; x += 2) {
			float y = (float) (Math.sin(frequency * (x + offset )) +
								Math.sin(frequency * (x*waveOffset + offset ) ))
							* amplitude + yOffset;
						
			

			verts[x * 4] = x * scale + xOffset; // x
			verts[x * 4 + 1] = y; // y
			verts[x * 4 + 2] = 0;
			verts[x * 4 + 3] = waterColor; // color

			verts[x * 4 + 4] = x * scale+ xOffset; // x
			verts[x * 4 + 5] = 0; // y
			verts[x * 4 + 6] = 0;
			verts[x * 4 + 7] = waterColor; // color
		}

		Mesh mesh = (new Mesh(true, precision * 4, 0, 
				new VertexAttribute(Usage.Position, 3, "a_position"), 
				new VertexAttribute(Usage.ColorPacked, 4, "a_color")));

		mesh.setVertices(verts);
		return mesh;
	}
	public boolean isComplete() {
		long localDt = TimeUtils.millis() - startTime;

		return localDt > 700;
	}

}
