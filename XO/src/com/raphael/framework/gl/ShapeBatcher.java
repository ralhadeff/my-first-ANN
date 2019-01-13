package com.raphael.framework.gl;

import android.opengl.GLES20;

import com.raphael.framework.impl.GLGraphics;

public class ShapeBatcher {
	final float[] verticesBuffer;
	short[] indices;
	int bufferIndex;
	final Vertices vertices;
	int vertexIndex, indexIndex;

	public ShapeBatcher(GLGraphics glGraphics, int maxVertices) {
		verticesBuffer = new float[maxVertices * 6];
		vertices = new Vertices(glGraphics, maxVertices, maxVertices * 2, true,
				false);
		bufferIndex = 0;
		vertexIndex = 0;
		indexIndex = 0;

		indices = new short[maxVertices * 2];
	}
	
	public void setShaderProgram(int shaderProgramHandle) {
		vertices.setShaderProgram(shaderProgramHandle);
	}
	
	public void setProjection(float[] projection){
		vertices.setProjection(projection);
	}

	public void beginBatch() {
		bufferIndex = 0;
		vertexIndex = 0;
		indexIndex = 0;
	}

	public void endBatch() {
		vertices.setIndices(indices, 0, indexIndex);
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		vertices.bind();
		vertices.draw(GLES20.GL_LINES, 0, indexIndex);
		vertices.unbind();
	}

	public void drawEmptyRectangle(float x, float y, float width, float height,
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

		indices[indexIndex++] = (short) (vertexIndex++);
		indices[indexIndex++] = (short) (vertexIndex);
		indices[indexIndex++] = (short) (vertexIndex++);
		indices[indexIndex++] = (short) (vertexIndex);
		indices[indexIndex++] = (short) (vertexIndex++);
		indices[indexIndex++] = (short) (vertexIndex);
		indices[indexIndex++] = (short) (vertexIndex++);
		indices[indexIndex++] = (short) (vertexIndex - 4);

	}

	public void drawEmptyCircle(float x, float y, float radius, float red,
			float green, float blue, float alpha) {

		for (int i = 0; i < 360; i += 4) {
			verticesBuffer[bufferIndex++] = (float) (x + radius
					* Math.cos(Math.toRadians(i)));
			verticesBuffer[bufferIndex++] = (float) (y + radius
					* Math.sin(Math.toRadians(i)));
			verticesBuffer[bufferIndex++] = red;
			verticesBuffer[bufferIndex++] = green;
			verticesBuffer[bufferIndex++] = blue;
			verticesBuffer[bufferIndex++] = alpha;

			indices[indexIndex++] = (short) (vertexIndex++);
			indices[indexIndex++] = (short) (vertexIndex);
		}

		// correct last index
		indices[indexIndex - 1] = (short) (vertexIndex - 90);

	}

	public void drawEmptyMultigon(float x, float y, float radius, int facets,
			float red, float green, float blue, float alpha) {

int angle = 360/facets;

		for (int i = 0; i < facets*angle; i += angle) {
			verticesBuffer[bufferIndex++] = (float) (x + radius
					* Math.cos(Math.toRadians(i)));
			verticesBuffer[bufferIndex++] = (float) (y + radius
					* Math.sin(Math.toRadians(i)));
			verticesBuffer[bufferIndex++] = red;
			verticesBuffer[bufferIndex++] = green;
			verticesBuffer[bufferIndex++] = blue;
			verticesBuffer[bufferIndex++] = alpha;

			indices[indexIndex++] = (short) (vertexIndex++);
			indices[indexIndex++] = (short) (vertexIndex);
		}

		// correct last index
		indices[indexIndex - 1] = (short) (vertexIndex - facets);

	}

	public void drawLine(float startX, float startY, float endX, float endY,
			float red, float green, float blue, float alpha) {

		verticesBuffer[bufferIndex++] = startX;
		verticesBuffer[bufferIndex++] = startY;
		verticesBuffer[bufferIndex++] = red;
		verticesBuffer[bufferIndex++] = green;
		verticesBuffer[bufferIndex++] = blue;
		verticesBuffer[bufferIndex++] = alpha;

		verticesBuffer[bufferIndex++] = endX;
		verticesBuffer[bufferIndex++] = endY;
		verticesBuffer[bufferIndex++] = red;
		verticesBuffer[bufferIndex++] = green;
		verticesBuffer[bufferIndex++] = blue;
		verticesBuffer[bufferIndex++] = alpha;

		indices[indexIndex++] = (short) (vertexIndex++);
		indices[indexIndex++] = (short) (vertexIndex++);

	}

}
