package com.mccullickgames.godownwiththeship.graphics;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Rectangle;
import com.mccullickgames.godownwiththeship.GameSettings;

public class OutOfBounds {
	private Mesh mesh;
	public void render() {
		mesh.render(GL10.GL_TRIANGLE_STRIP);
	}

	public void init(Rectangle boundaryRectangle) {
		float boundaryColor = GameSettings.BOUNDARY_COLOR;
		int verticesCount = 11*4;
		float[] verts = new float[verticesCount];
		float outerWidth = GameSettings.GAME_WIDTH;
		float outerHeight = GameSettings.GAME_HEIGHT;
		int i = 0;
		
		//LEFT
		verts[i++] = 0; // x
		verts[i++] = 0; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		verts[i++] = 0; // x
		verts[i++] = boundaryRectangle.height ; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		verts[i++] = -outerWidth ; // x
		verts[i++] = -outerHeight ; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		verts[i++] = -outerWidth ; // x
		verts[i++] = boundaryRectangle.height + outerHeight; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		//TOP
		verts[i++] = 0; // x
		verts[i++] = boundaryRectangle.height ; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		verts[i++] = boundaryRectangle.width+outerWidth; // x
		verts[i++] = boundaryRectangle.height + outerHeight ; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color		
		
		verts[i++] = boundaryRectangle.width; // x
		verts[i++] = boundaryRectangle.height ; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		//RIGHT
		verts[i++] = boundaryRectangle.width + outerWidth; // x
		verts[i++] = - outerHeight ; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		verts[i++] = boundaryRectangle.width; // x
		verts[i++] = 0 ; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		
		
		//BOTTOM
		
		
		verts[i++] = - outerWidth; // x
		verts[i++] = - outerHeight; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // color
		
		verts[i++] = 0; // x
		verts[i++] = 0 ; // y
		verts[i++] = 0;
		verts[i++] = boundaryColor; // colors
		
		
		
		
		
		
		
		
		

		mesh = (new Mesh(true, verticesCount, 0, 
				new VertexAttribute(Usage.Position, 3, "a_position"), 
				new VertexAttribute(Usage.ColorPacked, 4, "a_color")));

		mesh.setVertices(verts);
		
	}
}
