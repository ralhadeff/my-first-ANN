package com.raphael.framework.gl;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.raphael.framework.impl.GLGraphics;
import com.raphael.framework.math.Vector2;

public class Camera2D {
	public Vector2 position;
	public float zoom;
	public final float frustumWidth;
	public final float frustumHeight;
	final GLGraphics glGraphics;
	float[] projection = new float[16];

	public Camera2D(GLGraphics glGraphics, float frustumWidth,
			float frustumHeight) {
		this.glGraphics = glGraphics;
		this.frustumWidth = frustumWidth;
		this.frustumHeight = frustumHeight;
		this.position = new Vector2(frustumWidth / 2, frustumHeight / 2);
		this.zoom = 1.0f;
	}

	public void setViewportAndMatrices() {
		GLES20.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		Matrix.orthoM(projection, 0,position.x - frustumWidth * zoom / 2, position.x
				+ frustumWidth * zoom / 2, position.y - frustumHeight * zoom
				/ 2, position.y + frustumHeight * zoom / 2, 1, -1);
	}

	public void touchToWorld(Vector2 touch) {
		touch.x = (touch.x / (float) glGraphics.getWidth()) * frustumWidth
				* zoom;
		touch.y = (1 - touch.y / (float) glGraphics.getHeight())
				* frustumHeight * zoom;
		touch.add(position).sub(frustumWidth * zoom / 2,
				frustumHeight * zoom / 2);
	}

	public float[] getProjection() {
		return projection;
	}
}
