package com.raphael.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import com.raphael.framework.impl.GLGraphics;

public class RectangleBatcher {
	final float[] verticesBuffer;
	int bufferIndex;
	final Vertices vertices;
	int numRectangles;

	public RectangleBatcher(GLGraphics glGraphics, int maxRectangles) {
		verticesBuffer = new float[maxRectangles * 4 * 6];
		vertices = new Vertices(glGraphics, maxRectangles * 4,
				maxRectangles * 6, true, false);
		bufferIndex = 0;
		numRectangles = 0;

		short[] indices = new short[maxRectangles * 6];
		int len = indices.length;
		short j = 0;
		for (int i = 0; i < len; i += 6, j += 4) {
			indices[i + 0] = (short) (j + 0);
			indices[i + 1] = (short) (j + 1);
			indices[i + 2] = (short) (j + 2);
			indices[i + 3] = (short) (j + 2);
			indices[i + 4] = (short) (j + 3);
			indices[i + 5] = (short) (j + 0);
		}
		vertices.setIndices(indices, 0, indices.length);
	}

	public void beginBatch() {
		numRectangles = 0;
		bufferIndex = 0;
	}

	public void endBatch() {
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		vertices.bind();
		vertices.draw(GL10.GL_TRIANGLES, 0, numRectangles * 6);
		vertices.unbind();
	}

	public void drawRectangle(float x, float y, float width, float height,
			float red, float green, float blue, float alpha) {
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float x1 = x - halfWidth;
		float y1 = y - halfHeight;
		float x2 = x + halfWidth;
		float y2 = y + halfHeight;

		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = red;
		verticesBuffer[bufferIndex++] = green;
		verticesBuffer[bufferIndex++] = blue;
		verticesBuffer[bufferIndex++] = alpha;

		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = red;
		verticesBuffer[bufferIndex++] = green;
		verticesBuffer[bufferIndex++] = blue;
		verticesBuffer[bufferIndex++] = alpha;

		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = red;
		verticesBuffer[bufferIndex++] = green;
		verticesBuffer[bufferIndex++] = blue;
		verticesBuffer[bufferIndex++] = alpha;

		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = red;
		verticesBuffer[bufferIndex++] = green;
		verticesBuffer[bufferIndex++] = blue;
		verticesBuffer[bufferIndex++] = alpha;

		numRectangles++;
	}
	
	public void drawComplexRectangle(float x, float y, float width, float height,
			float[] colorMatrix) {
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float x1 = x - halfWidth;
		float y1 = y - halfHeight;
		float x2 = x + halfWidth;
		float y2 = y + halfHeight;

		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = colorMatrix[0];
		verticesBuffer[bufferIndex++] = colorMatrix[1];
		verticesBuffer[bufferIndex++] = colorMatrix[2];
		verticesBuffer[bufferIndex++] = colorMatrix[3];

		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = colorMatrix[4];
		verticesBuffer[bufferIndex++] = colorMatrix[5];
		verticesBuffer[bufferIndex++] = colorMatrix[6];
		verticesBuffer[bufferIndex++] = colorMatrix[7];

		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = colorMatrix[8];
		verticesBuffer[bufferIndex++] = colorMatrix[9];
		verticesBuffer[bufferIndex++] = colorMatrix[10];
		verticesBuffer[bufferIndex++] = colorMatrix[11];

		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = colorMatrix[12];
		verticesBuffer[bufferIndex++] = colorMatrix[13];
		verticesBuffer[bufferIndex++] = colorMatrix[14];
		verticesBuffer[bufferIndex++] = colorMatrix[15];

		numRectangles++;
	}

}
