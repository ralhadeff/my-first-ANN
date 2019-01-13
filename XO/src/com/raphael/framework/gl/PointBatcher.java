package com.raphael.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import com.raphael.framework.impl.GLGraphics;

public class PointBatcher {
	final float[] verticesBuffer;
	int bufferIndex;
	final Vertices vertices;

	public PointBatcher(GLGraphics glGraphics, int maxVertices) {
		verticesBuffer = new float[maxVertices * 6];
		vertices = new Vertices(glGraphics, maxVertices, 0, true, false);
		bufferIndex = 0;
	}

	public void beginBatch() {
		bufferIndex = 0;
	}

	public void endBatch() {
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		vertices.bind();
		vertices.draw(GL10.GL_POINTS, 0, bufferIndex / 6);
		vertices.unbind();
	}

	public void drawPoint(float x, float y, float red, float green, float blue,
			float alpha) {

		verticesBuffer[bufferIndex++] = x;
		verticesBuffer[bufferIndex++] = y;
		verticesBuffer[bufferIndex++] = red;
		verticesBuffer[bufferIndex++] = green;
		verticesBuffer[bufferIndex++] = blue;
		verticesBuffer[bufferIndex++] = alpha;
	}

}
