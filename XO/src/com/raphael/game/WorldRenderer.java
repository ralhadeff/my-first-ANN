package com.raphael.game;

import android.opengl.GLES20;

import com.raphael.framework.gl.Camera2D;
import com.raphael.framework.gl.ShapeBatcher;
import com.raphael.framework.gl.SpriteBatcher;
import com.raphael.framework.impl.GLGraphics;
import com.raphael.framework.math.Rectangle;
import com.raphael.framework.math.Vector2;

enum SizeType {
	FIXED, SINUS_ARCH, LINEAR_INCREASE, EXPLODING
}

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 1080;
	static final float FRUSTUM_HEIGHT = 1920;
	GLGraphics glGraphics;
	World world;
	Camera2D cam;
	ShapeBatcher shapeBatcher;
	SpriteBatcher batcher;
	Vector2 view;

	public WorldRenderer(GLGraphics glGraphics, World world,
			SpriteBatcher batcher, ShapeBatcher shapeBatcher) {
		this.glGraphics = glGraphics;
		this.world = world;
		this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.shapeBatcher = shapeBatcher;
		this.batcher=batcher;
		view = new Vector2(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2);

	}

	public void render() {

		cam.setViewportAndMatrices();
		cam.position = view;

		GLES20.glClearColor(1, 1, 1, 1);

		GLES20.glDisable(GLES20.GL_TEXTURE_2D);
		shapeBatcher.setProjection(cam.getProjection());
		shapeBatcher.beginBatch();
		GLES20.glLineWidth(12);
		renderBars();
		renderGrid();
		renderEnd();
		shapeBatcher.endBatch();
		//GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		//batcher.setProjection(cam.getProjection());
		//batcher.beginBatch(Assets.test);
		//batcher.drawSprite(540, 980, 600, 600, Assets.region);
		//int counter = 0;
		//for (Tile tile : world.grid.grid){
		//	if (tile.type ==Type.CLEAR)
		//		counter++;
		//}
		//batcher.addGreen(counter/5.0f);
		//batcher.endBatch();
	}

	private void renderEnd() {
		if (world.win) {
			shapeBatcher.drawEmptyRectangle(540, 540, 200, 300,
					0.3f, 1, 0.3f, 1);
			shapeBatcher.drawEmptyRectangle(600, 765, 60, 150,
					0.3f, 1, 0.3f, 1);	
			shapeBatcher.drawLine(440, 490, 580, 490, 0.3f, 1, 0.3f, 1);	
			shapeBatcher.drawLine(440, 590, 580, 590, 0.3f, 1, 0.3f, 1);	
		} else if (world.lose) {
			// L
			shapeBatcher.drawLine(140, 800, 140, 600, 1, 0, 0, 1);	
			shapeBatcher.drawLine(140, 600, 240, 600, 1, 0, 0, 1);	
			// O
			shapeBatcher.drawEmptyCircle(390, 700, 100, 1, 0, 0, 1)	;
			// Z
			shapeBatcher.drawLine(540, 600, 640, 600, 1, 0, 0, 1);	
			shapeBatcher.drawLine(540, 800, 640, 800, 1, 0, 0, 1);
			shapeBatcher.drawLine(540, 600, 640, 800, 1, 0, 0, 1);	
			// E
			shapeBatcher.drawLine(690, 800, 690, 600, 1, 0, 0, 1);	
			shapeBatcher.drawLine(690, 600, 790, 600, 1, 0, 0, 1);	
			shapeBatcher.drawLine(690, 700, 790, 700, 1, 0, 0, 1);
			shapeBatcher.drawLine(690, 800, 790, 800, 1, 0, 0, 1);
			// R
			shapeBatcher.drawLine(840, 600, 840, 800, 1, 0, 0, 1);	
			shapeBatcher.drawEmptyCircle(890, 750, 50, 1, 0, 0, 1)	;
			shapeBatcher.drawLine(840, 700, 940, 600, 1, 0, 0, 1);	
		}
	}

	private void renderBars() {
		for (Rectangle rectangle : world.rectangles) {
			shapeBatcher.drawEmptyRectangle(rectangle.lowerLeft.x
					+ rectangle.width / 2, rectangle.lowerLeft.y
					+ rectangle.height / 2, rectangle.width, rectangle.height,
					0, 0, 0, 1);
		}
		drawX(-1, 3);
		drawO(1,3);
		
		shapeBatcher.drawLine(150, 1400, 930, 1400, 1, 0, 0, 1);
		shapeBatcher.drawLine(150, 1140, 930, 1140, 1, 0, 0, 1);
		shapeBatcher.drawLine(410, 880, 410, 1660, 1, 0, 0, 1);
		shapeBatcher.drawLine(670, 880, 670, 1660, 1, 0, 0, 1);
		
		if (world.player == Type.X)
			drawX(0,4);
		else
			drawO(0,4);
	}

	private void renderGrid() {
		for (Tile tile : world.grid.grid) {
			switch (tile.type) {
			case X:
				drawX(tile.x, tile.y);
				break;
			case O:
				drawO(tile.x, tile.y);
				break;
			}
		}
	}

	private Vector2 tile2World(int x, int y) {
		Vector2 coords = new Vector2(540, 1270);
		coords.add(x * 300, -y * 300);
		return coords;
	}

	private void drawO(int x, int y) {
		Vector2 coords = tile2World(x, y);
		shapeBatcher.drawEmptyCircle(coords.x, coords.y, 50, 1, 0, 1, 1);
	}

	private void drawX(int x, int y) {
		Vector2 coords = tile2World(x, y);
		shapeBatcher.drawLine(coords.x - 50, coords.y - 50, coords.x + 50,
				coords.y + 50, 0, 0, 0, 1);
		shapeBatcher.drawLine(coords.x - 50, coords.y + 50, coords.x + 50,
				coords.y - 50, 0, 0, 0, 1);
	}

}
